/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.hospedagem;

import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.exception.DataForaDoPrazoException;
import br.com.hotel.reservashospedagens.exception.HospedagemNaoExisteException;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.QuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.HospedagemRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HospedagemModel {

    @Autowired
    HospedagemRepositorio hospedagemRepositorio;
    @Autowired
    TipoQuartoModel tipoQuartoModel;
    @Autowired
    QuartoModel quartoModel;
    @Autowired
    CobrancaModel cobrancaModel;

    public HospedagemEntity encontrarPorId(int id) throws HospedagemNaoExisteException {
        Optional<HospedagemEntity> container = hospedagemRepositorio.findById(id);
        return container.orElseThrow(HospedagemNaoExisteException::new);
    }

    @Transactional(rollbackFor = {DataForaDoPrazoException.class})
    public void checkin(ReservaEntity reservaEntity, LocalDate checkout) throws DataForaDoPrazoException {
        LocalDate hoje = LocalDate.now();
        if (!this.entradaESaidaDentroDoPrazo(hoje, checkout, reservaEntity.getCheckin(), reservaEntity.getCheckout())) {
            throw new DataForaDoPrazoException();
        }
        HospedagemEntity hospedagemEntity = new HospedagemEntity(reservaEntity.getCliente(), HospedagemStatusEnum.INICIADA, hoje, checkout);
        double valor = cobrancaModel.todosOsQuartosReservados(reservaEntity.getQuartosReservados(), reservaEntity.getCheckin(), reservaEntity.getCheckout());
        hospedagemEntity.setValor(valor);
        List<QuartoEntity> quartosSelecionados = this.selecionarQuartosParaHospedagem(reservaEntity.getQuartosReservados());
        hospedagemEntity.incluirQuartos(quartosSelecionados);
        reservaEntity.getCliente().incluirHospedagem(hospedagemEntity);
        hospedagemRepositorio.save(hospedagemEntity);

    }

    public List<HospedagemEntity> encontrarHospedagensIniciadas(ClienteEntity clienteEntity) throws HospedagemNaoExisteException {
        List<HospedagemEntity> hospedagens = hospedagemRepositorio.todasHospedagensIniciadas(clienteEntity, HospedagemStatusEnum.INICIADA);
        if (hospedagens.isEmpty()) {
            throw new HospedagemNaoExisteException();
        }
        return hospedagens;
    }

    @Transactional
    public void checkout(HospedagemEntity hospedagem) throws HospedagemNaoExisteException {
        LocalDate checkout = LocalDate.now();
        hospedagem.setCheckout(checkout);
        hospedagem.setStatus(HospedagemStatusEnum.ENCERRADA);
    }

    public boolean dentroDoPrazo(LocalDate data, LocalDate checkin, LocalDate checkout) {

        return (data.isAfter(checkin) || data.isEqual(checkin)) && (data.isBefore(checkout) || data.isEqual(checkout));

    }

    public boolean entradaESaidaDentroDoPrazo(LocalDate entradaEfetiva, LocalDate saidaEfetiva, LocalDate checkin, LocalDate checkout) {
        boolean entrada = this.dentroDoPrazo(entradaEfetiva, checkin, checkout);
        boolean saida = this.dentroDoPrazo(saidaEfetiva, checkin, checkout);
        return entrada && saida;
    }

    public HospedagemEntity encontrarPorQuarto(QuartoEntity quartoEntity) {
        LocalDate agora = LocalDate.now();
        HospedagemEntity hospedagemEntity = hospedagemRepositorio.findByQuarto(quartoEntity, agora);
        return hospedagemEntity;
    }

    @Transactional
    public void incluirServico(HospedagemEntity hospedagem, ServicoEntity servico) {
        HospedagemServicoEntity hs = new HospedagemServicoEntity(hospedagem, servico, LocalDate.now());
        hospedagem.incluirServico(hs);
    }

    public List<QuartoEntity> encontrarQuartosLivres() {
        List<QuartoEntity> todosOsQuartos = quartoModel.listarTodos();
        List<QuartoEntity> quartosOcupados = this.encontrarQuartosOcupados();
        todosOsQuartos.removeAll(quartosOcupados);
        return todosOsQuartos;
    }

    public List<QuartoEntity> encontrarQuartosOcupados() {
        LocalDate agora = LocalDate.now();
        return hospedagemRepositorio.encontrarQuartosOcupadosNesteMomento(agora);
    }

    public List<QuartoEntity> selecionarQuartosParaHospedagem(Collection<ReservaTipoQuarto> tiposReservados) {
        List<QuartoEntity> quartosEscolhidos = new ArrayList<>();
        List<QuartoEntity> quartosLivres = this.encontrarQuartosLivres();
        //ordena por andar
        quartosLivres = quartosLivres.stream().sorted(Comparator.comparing(QuartoEntity::getAndar)).collect(Collectors.toList());

        Iterator<ReservaTipoQuarto> iterator = tiposReservados.iterator();
        while (iterator.hasNext()) {
            ReservaTipoQuarto tipoReservado = iterator.next();
            int quantidade = tipoReservado.getQuantidade();
            List<QuartoEntity> quartos = quartosLivres.stream()
                    .filter(quarto -> quarto.getTipo().equals(tipoReservado.getId().getTipoQuartoId()))
                    .limit(quantidade)
                    .collect(Collectors.toList());

            quartosEscolhidos.addAll(quartos);
        }
        return quartosEscolhidos;

    }
}
