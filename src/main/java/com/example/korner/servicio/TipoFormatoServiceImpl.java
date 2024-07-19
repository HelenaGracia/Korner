package com.example.korner.servicio;

import com.example.korner.modelo.TipoFormatos;
import com.example.korner.repositorios.TipoFormatoRepository;

public class TipoFormatoServiceImpl extends AbstractService<TipoFormatos,Integer, TipoFormatoRepository>{

    public TipoFormatoServiceImpl(TipoFormatoRepository tipoFormatoRepository) {
        super(tipoFormatoRepository);
    }
}
