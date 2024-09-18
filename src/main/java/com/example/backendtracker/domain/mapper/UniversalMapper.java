package com.example.backendtracker.domain.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.modelmapper.TypeMap;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UniversalMapper {

    private final ModelMapper modelMapper;

    public UniversalMapper() {
        this.modelMapper = new ModelMapper();
    }

    public <D, T> D map(T source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    public <D, T> List<D> mapList(List<T> source, Class<D> destinationType) {
        return source.stream()
                .map(element -> modelMapper.map(element, destinationType))
                .collect(Collectors.toList());
    }

    public <D, T> D mapToRecord(T source, Class<D> recordType) {
        TypeMap<T, D> typeMap = modelMapper.getTypeMap((Class<T>) source.getClass(), recordType);

        if (typeMap == null) {
            modelMapper.createTypeMap((Class<T>) source.getClass(), recordType);
        }

        return modelMapper.map(source, recordType);
    }

    public <D, T> List<D> mapListToRecord(List<T> source, Class<D> recordType) {
        return source.stream()
                .map(element -> mapToRecord(element, recordType))
                .collect(Collectors.toList());
    }


}
