package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipService {

    final ShipRepository shipRepository;
    final ModelMapper modelMapper;

    public ShipService(ShipRepository shipRepository, ModelMapper modelMapper) {
        this.shipRepository = shipRepository;
        this.modelMapper = modelMapper;
    }

    public ShipDTO createNewShip(ShipDTO shipData){
        ShipEntity shipEntity = modelMapper.map(shipData, ShipEntity.class);
        return  modelMapper.map(shipRepository.save(shipEntity), ShipDTO.class);
    }

    public ShipDTO getShipDetailsById(Long shipId){
        ShipEntity shipEntity = shipRepository.findById(shipId).orElseThrow(() -> new ResourceNotFoundException("Ship not found with ID: " + shipId));
        return  modelMapper.map(shipEntity, ShipDTO.class);
    }

    public List<ShipDTO> getAllShipDetails(){
        List<ShipEntity> list = shipRepository.findAll();
        return list.stream().map(shipEntity -> modelMapper.map(shipEntity, ShipDTO.class)).collect(Collectors.toList());
    }

    public ShipDTO updateShipDetailsById(Long id, ShipDTO shipData){
        Optional<ShipEntity> optional = shipRepository.findById(id);
        if(optional.isPresent()){
            ShipEntity entity = optional.get();
            if (shipData.getName() != null) entity.setName(shipData.getName());
            if (shipData.getType() != null) entity.setType(shipData.getType());
            if (shipData.getCapacity() != null) entity.setCapacity(shipData.getCapacity());
            if (shipData.getStatus() != null) entity.setStatus(shipData.getStatus());

            ShipEntity updated = shipRepository.save(entity);
            return modelMapper.map(updated, ShipDTO.class);
        }
        return null;
    }

    public void deleteShipDetailById(Long shipId){
        if (!shipRepository.existsById(shipId)) {
            throw new ResourceNotFoundException("Ship not found with ID: " + shipId);
        }
        shipRepository.deleteById(shipId);

    }
}
