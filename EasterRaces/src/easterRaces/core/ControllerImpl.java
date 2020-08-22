package easterRaces.core;

import easterRaces.core.interfaces.Controller;
import easterRaces.entities.cars.Car;
import easterRaces.entities.cars.MuscleCar;
import easterRaces.entities.cars.SportsCar;
import easterRaces.entities.drivers.Driver;
import easterRaces.entities.drivers.DriverImpl;
import easterRaces.entities.racers.Race;
import easterRaces.entities.racers.RaceImpl;
import easterRaces.repositories.interfaces.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static easterRaces.common.ExceptionMessages.*;
import static easterRaces.common.OutputMessages.*;
import static easterRaces.common.OutputMessages.DRIVER_THIRD_POSITION;

public class ControllerImpl implements Controller {
    private Repository<Driver> driverRepository;
    private Repository<Car> carRepository;
    private Repository<Race> raceRepository;


    public ControllerImpl(Repository<Driver> riderRepository, Repository<Car> motorcycleRepository, Repository<Race> raceRepository) {
        this.driverRepository = riderRepository;
        this.carRepository = motorcycleRepository;
        this.raceRepository = raceRepository;
    }

    @Override
    public String createDriver(String driver) {
        if (this.driverRepository.getByName(driver) != null) {
            throw new IllegalArgumentException(String.format(DRIVER_EXISTS, driver));
        }
        Driver rider = new DriverImpl(driver);
        this.driverRepository.add(rider);

        return String.format(DRIVER_CREATED, driver);
    }

    @Override
    public String createCar(String type, String model, int horsePower) {
        if (carRepository.getByName(model) != null) {
            throw new IllegalArgumentException(String.format(CAR_EXISTS, model));
        }

        Car car;

        if (type.equals("Muscle")) {
            car = new MuscleCar(model, horsePower);
        }
        else {
            car = new SportsCar(model, horsePower);
        }

        this.carRepository.add(car);
        return String.format(CAR_CREATED, car.getClass().getSimpleName(), model);
    }

    @Override
    public String addCarToDriver(String driverName, String carModel) {
        Car car = carRepository.getByName(carModel);
        Driver driver = driverRepository.getByName(driverName);

        if (driver == null) {
            throw new NullPointerException(String.format(DRIVER_NOT_FOUND, driverName));
        }
        if (car == null) {
            throw new NullPointerException(String.format(CAR_NOT_FOUND, carModel));
        }
        driver.addCar(car);
        return String.format(CAR_ADDED, driverName, carModel);
    }

    @Override
    public String addDriverToRace(String raceName, String driverName) {
        Race race = raceRepository.getByName(raceName);
        Driver driver = driverRepository.getByName(driverName);

        if (race == null) {
            throw new NullPointerException(String.format(RACE_NOT_FOUND, raceName));
        }
        if (driver == null) {
            throw new NullPointerException(String.format(DRIVER_NOT_FOUND, driverName));
        }
        race.addDriver(driver);
        return String.format(DRIVER_ADDED, driverName, raceName);
    }

    @Override
    public String startRace(String raceName) {
        Race race = raceRepository.getByName(raceName);

        if (race == null) {
            throw new NullPointerException(String.format(RACE_NOT_FOUND, raceName));
        }

        if (race.getDrivers().size() < 3 ) {
            throw new IllegalArgumentException(String.format(RACE_INVALID, raceName, 3));
        }

        List<Driver> winners = race.getDrivers().stream()
                .sorted((r1, r2) ->
                        Double.compare(r2.getCar().calculateRacePoints(race.getLaps()),
                                r1.getCar().calculateRacePoints(race.getLaps())))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb
                .append(String.format(DRIVER_FIRST_POSITION, winners.get(0).getName(), raceName))
                .append(System.lineSeparator())
                .append(String.format(DRIVER_SECOND_POSITION, winners.get(1).getName(), raceName))
                .append(System.lineSeparator())
                .append(String.format(DRIVER_THIRD_POSITION, winners.get(2).getName(), raceName));
        return sb.toString().trim();
    }

    @Override
    public String createRace(String name, int laps) {
        if (this.raceRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(RACE_EXISTS, name));
        }

        Race race = new RaceImpl(name, laps);
        this.raceRepository.add(race);
        return String.format(RACE_CREATED, name);
    }
}
