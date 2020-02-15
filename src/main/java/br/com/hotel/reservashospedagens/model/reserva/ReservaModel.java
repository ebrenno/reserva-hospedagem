/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.reserva;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoNaoExisteException;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.QuartoModel;
import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoEnum;
import br.com.hotel.reservashospedagens.dto.reserva.VagaDto;
import br.com.hotel.reservashospedagens.exception.NaoHaVagasException;
import br.com.hotel.reservashospedagens.exception.ReservaNaoExisteException;
import br.com.hotel.reservashospedagens.model.pagamento.CobrancaModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.ClienteEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuartoId;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import br.com.hotel.reservashospedagens.persistencia.repositorio.ReservaRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaModel {

    Log log = LogFactory.getLog(ReservaModel.class);

    @Autowired
    ReservaRepositorio reservaRepositorio;
    @Autowired
    TipoQuartoModel tipoQuartoModel;
    @Autowired
    QuartoModel quartoModel;
    @Autowired
    CobrancaModel cobrancaModel;

    public int reservadosEntre(TipoQuartoEntity tipo, LocalDate checkin, LocalDate checkout) {
        Collection<ReservaEntity> reservas = reservaRepositorio.findReservasBetween(checkin, checkout);

        int quantidade = reservas.stream().flatMap(reserva -> reserva.getQuartosReservados().stream())
                .filter(quarto -> quarto.getId().getTipoQuartoId().equals(tipo))
                .mapToInt(quarto -> quarto.getQuantidade())
                .reduce(0, (soma, valor) -> soma + valor);
        return quantidade;
    }

    public boolean estaDisponivel(TipoQuartoEntity tipoQuarto, LocalDate checkin, LocalDate checkout) {
        int quartosReservados = this.reservadosEntre(tipoQuarto, checkin, checkout);
        int quartosDisponiveis = quartoModel.contarPorTipo(tipoQuarto);
        return quartosDisponiveis > quartosReservados;
    }

    public int vagasRestantes(TipoQuartoEntity tipoQuarto, LocalDate checkin, LocalDate checkout) throws TipoQuartoNaoExisteException {
        int quartosReservados = this.reservadosEntre(tipoQuarto, checkin, checkout);
        int quartosDisponiveis = quartoModel.contarPorTipo(tipoQuarto);
        return quartosDisponiveis - quartosReservados;
    }

    @Transactional(rollbackFor = {TipoQuartoNaoExisteException.class, NaoHaVagasException.class})
    public void iniciar(ClienteEntity cliente, Collection<VagaDto> vagas, LocalDate checkin, LocalDate checkout) throws TipoQuartoNaoExisteException, NaoHaVagasException {
        ReservaEntity reservaEntity = new ReservaEntity(cliente, checkin, checkout);

        for (VagaDto vaga : vagas) {
            TipoQuartoEntity tipoQuarto = tipoQuartoModel.getEntity(vaga.getTipoQuarto());
            int vagasRestantes = this.vagasRestantes(tipoQuarto, checkin, checkout);
            if (vaga.getQuantidade() > vagasRestantes) {
                log.info("não há vagas disponiveis em" + vaga + " para o cliente[" + cliente + "]");
                throw new NaoHaVagasException();
            }
            TipoQuartoEntity tipoQuartoEntity = tipoQuartoModel.getEntity(vaga.getTipoQuarto());
            ReservaTipoQuartoId reservaTipoQuartoId = new ReservaTipoQuartoId(reservaEntity, tipoQuartoEntity);
            ReservaTipoQuarto reservaTipoQuarto = new ReservaTipoQuarto(reservaTipoQuartoId, vaga.getQuantidade());
            reservaEntity.addTipoQuarto(reservaTipoQuarto);
            log.info("reserva aplicada para cliente: " + cliente.getId());
        }
        double valor = cobrancaModel.daReserva(checkin, checkout);
        reservaEntity.setValor(valor);
        reservaEntity.setStatus(ReservaStatusEnum.AGENDADA);
        cliente.incluirReserva(reservaEntity);
        reservaRepositorio.save(reservaEntity);

    }

    public ReservaEntity encontrarPorId(int id) throws ReservaNaoExisteException {
        Optional<ReservaEntity> container = reservaRepositorio.findById(id);
        return container.orElseThrow(ReservaNaoExisteException::new);
    }

    @Transactional
    public Collection<VagaDto> todasVagasRestantes(LocalDate checkin, LocalDate checkout) throws TipoQuartoNaoExisteException {
        Collection<VagaDto> vagas = new ArrayList<>();
        for (TipoQuartoEnum tipo : TipoQuartoEnum.values()) {
            TipoQuartoEntity tipoQuarto = tipoQuartoModel.getEntity(tipo);
            int quantidade = this.vagasRestantes(tipoQuarto, checkin, checkout);
            VagaDto vaga = new VagaDto(tipo, quantidade,tipoQuarto.getPreco());
            vagas.add(vaga);
            log.info("encontrado[" + quantidade + "] vagas para: " + tipo.toString());
        }
        log.info("fim da busca por vagas");
        return vagas;
    }

}
