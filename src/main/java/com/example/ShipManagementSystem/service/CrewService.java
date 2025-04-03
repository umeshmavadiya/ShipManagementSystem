package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.CrewEntity;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.CrewRepository;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrewService {
    private final CrewRepository crewRepository;
    private final ShipRepository shipRepository;
    final ModelMapper modelMapper;


    public CrewService(CrewRepository crewRepository, ShipRepository shipRepository,  ModelMapper modelMapper) {
        this.crewRepository = crewRepository;
        this.shipRepository = shipRepository;
        this.modelMapper = modelMapper;
    }
    public CrewDTO assignCrewToShip(Long shipId, CrewDTO crew) {
        ShipEntity ship = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship with ID " + shipId + " not found"));
        ShipDTO shipDTO = modelMapper.map(ship, ShipDTO.class);
        crew.setShip(shipDTO);
        CrewEntity crewEntity = modelMapper.map(crew, CrewEntity.class);
        crewEntity.setShip(ship);
        return  modelMapper.map(crewRepository.save(crewEntity), CrewDTO.class);


    }

    public List<CrewDTO> getCrewByShip(Long shipId) {
        List<CrewEntity> list = crewRepository.findAll().stream()
                .filter(crew -> crew.getShip().getShipId().equals(shipId))
                .toList();
        return list.stream().map(crewEntity -> modelMapper.map(crewEntity, CrewDTO.class)).collect(Collectors.toList());
    }
    public CrewDTO getCrewByShipIdAndCrewId(Long shipId ,Long crewId) {
        CrewEntity crewData = crewRepository.findCrewByShipIdAndCrewId(shipId,crewId);
        return  modelMapper.map(crewData, CrewDTO.class);
    }

    public CrewDTO updateCrewPosition(Long crewId, String newPosition) {
        CrewEntity crewEntity= crewRepository.findById(crewId)
                .map(crew -> {
                    crew.setPosition(newPosition);
                    return crewRepository.save(crew);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Crew with ID " + crewId + " not found"));
        return  modelMapper.map(crewEntity, CrewDTO.class);
    }

    public void removeCrewFromShip(Long crewId) {
        if(! crewRepository.existsById(crewId)){
            throw new ResourceNotFoundException("Crew not found with ID: " + crewId);
        }
        crewRepository.deleteById(crewId);
    }

}
