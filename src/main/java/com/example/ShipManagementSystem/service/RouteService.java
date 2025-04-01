package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.RouteEntity;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.RouteRepository;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private  final ShipRepository shipRepository;
    final ModelMapper modelMapper;

    public RouteService(RouteRepository routeRepository, ShipRepository shipRepository, ModelMapper modelMapper) {
        this.routeRepository = routeRepository;
        this.shipRepository = shipRepository;
        this.modelMapper = modelMapper;
    }
        public RouteDTO assignRouteToShip(Long shipId, RouteDTO route) {
        ShipEntity ship = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("Ship with ID " + shipId + " not found"));
        ShipDTO shipDTO = modelMapper.map(ship, ShipDTO.class);
        route.setShip(shipDTO);
        RouteEntity routeEntity = modelMapper.map(route, RouteEntity.class);
            routeEntity.setShip(ship);
        return modelMapper.map(routeRepository.save(routeEntity),RouteDTO.class);
    }

    public List<RouteDTO> getRoutesByShip(Long shipId) {
        List<RouteEntity> list= routeRepository.findAll().stream()
                .filter(route -> route.getShip().getShipId().equals(shipId))
                .toList();
        return list.stream().map(routeEntity -> modelMapper.map(routeEntity, RouteDTO.class)).collect(Collectors.toList());
    }
    public  RouteDTO getRouteByShipIdAndRouteId(Long shipId,Long routeId){
        RouteEntity routData =routeRepository.findRouteByShipIdAndRouteId(shipId,routeId);
        return modelMapper.map(routData, RouteDTO.class);
    }

    public RouteDTO updateRoute(Long routeId, RouteDTO updatedRoute) {
            RouteEntity routeEntity = routeRepository.findById(routeId)
                .map(route -> {
                    route.setPorts(updatedRoute.getPorts());
                    route.setDistance(updatedRoute.getDistance());
                    route.setEstimatedTime(updatedRoute.getEstimatedTime());
                    route.setDepartureTime(updatedRoute.getDepartureTime());
                    route.setArrivalTime(updatedRoute.getArrivalTime());
                    return routeRepository.save(route);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Route with ID " + routeId + " not found"));
            return  modelMapper.map(routeEntity, RouteDTO.class);
    }

    public void deleteRoute(Long routeId) {
        if(!routeRepository.existsById(routeId)){
            throw new ResourceNotFoundException("Route not found with ID: " + routeId);
        }
        routeRepository.deleteById(routeId);
    }

}
