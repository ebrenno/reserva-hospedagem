/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.hotel.reservashospedagens.model.pagamento;

import br.com.hotel.reservashospedagens.model.quarto.TipoQuartoModel;
import br.com.hotel.reservashospedagens.persistencia.entidade.HospedagemServicoEntity;
import br.com.hotel.reservashospedagens.persistencia.entidade.ReservaTipoQuarto;
import br.com.hotel.reservashospedagens.persistencia.entidade.TipoQuartoEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CobrancaModel {

    @Autowired
    TipoQuartoModel tipoQuartoModel;

    public double todosOsQuartosReservados(Set<ReservaTipoQuarto> quartos, LocalDate checkin, LocalDate checkout) {
        int dias = contarDias(checkin, checkout);
        double total = 0;
        for (ReservaTipoQuarto quarto : quartos) {
            TipoQuartoEntity entity = quarto.getId().getTipoQuartoId();
            total += entity.getPreco() * quarto.getQuantidade() * dias;
        }
        return total;
    }

    public double daReserva(LocalDate checkin, LocalDate checkout) {

        int dias = contarDias(checkin, checkout);
        //taxa cobrada por dia reservado
        return dias * 30;
    }

    public int contarDias(LocalDate checkin, LocalDate checkout) {
        return checkin.until(checkout).getDays();
    }

    public double dosServicos(List<HospedagemServicoEntity> servicos) {
        double total = servicos.stream()
                .map(hospedagemServico -> hospedagemServico.getServico().getValor())
                .reduce(0d, (soma, valor) -> soma + valor);
        return total;
    }
}
