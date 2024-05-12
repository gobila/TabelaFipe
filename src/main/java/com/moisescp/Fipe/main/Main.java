package com.moisescp.Fipe.main;

import com.moisescp.Fipe.model.BrandData;
import com.moisescp.Fipe.model.Data;
import com.moisescp.Fipe.model.VehicleData;
import com.moisescp.Fipe.service.DataConverter;
import com.moisescp.Fipe.service.GetApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private Scanner userInput = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1";

    private  GetApi getApi = new GetApi();
    private DataConverter converter =  new DataConverter();


    public void showMenu(){
        var menu = """
                *** OPÇÕES ***
                Carros
                Motos
                Caminhões
                
                Digite uma das opções para consulta:
                """;
        System.out.println(menu);

        var option =  userInput.nextLine();

        String endpoint;

        if(option.toLowerCase().contains("carr")){
            endpoint = URL_BASE + "/carros/marcas";
        }else if (option.toLowerCase().contains("mot")){
            endpoint = URL_BASE + "/motos/marcas";
        }else{
            endpoint = URL_BASE + "/caminhoes/marcas";
        }

        var json = getApi.getData(endpoint);
        System.out.println(json);

        var brands = converter.getList(json, Data.class);
        brands.stream().sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var brandCode = userInput.nextLine();
        endpoint = endpoint + "/" + brandCode + "/modelos" ;
        json = getApi.getData(endpoint);

        var modelList = converter.getData(json, BrandData.class);
        System.out.println("\nModelos: ");
        modelList.models().stream()
                .sorted(Comparator.comparing(Data::code))
                .forEach(System.out::println);

        System.out.println("\nDigite o nome do carro que vc deseja buscar: ");
        var vehicleModel = userInput.nextLine();
        List<Data> filteredModels = modelList.models().stream()
                .filter(m->m.name().toLowerCase().contains(vehicleModel.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados: ");
        filteredModels.forEach(System.out::println);

        System.out.println("\nDigite o código do modelo desejado: ");
        var vehicleCode = userInput.nextLine();
        endpoint = endpoint + "/" + vehicleCode + "/anos";

        json = getApi.getData(endpoint);
        List<Data> modelsByYears = converter.getList(json, Data.class);
        List<VehicleData> vehicles = new ArrayList<>();

        for(int i = 0; i < modelsByYears.size(); i++){
            var yearsEndpoint = endpoint + "/" + modelsByYears.get(i).code();
            json = getApi.getData(yearsEndpoint);
            VehicleData vehicle = converter.getData(json, VehicleData.class);
            vehicles.add(vehicle);
        }
        System.out.println("Todos os veículos filtrados por ano: ");
        vehicles.forEach(System.out::println);


    }

}
