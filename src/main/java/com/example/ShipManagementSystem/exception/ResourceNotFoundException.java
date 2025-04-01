package com.example.ShipManagementSystem.exception;

public class ResourceNotFoundException  extends RuntimeException{
  public ResourceNotFoundException(String message){
      super(message);
  }
}
