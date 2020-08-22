package easterRaces.entities.drivers;

import easterRaces.entities.cars.Car;

import static easterRaces.common.ExceptionMessages.CAR_INVALID;
import static easterRaces.common.ExceptionMessages.INVALID_NAME;

public class DriverImpl implements Driver {
    private static final boolean DEFAULT_PARTICIPATION = false;
    private static final int INITIAL_WINS = 0;
    private static final Car INITIAL_CAR = null;
    
    private String name;
    private Car car;
    private int numberOfWins;
    private boolean canParticipate;

    public DriverImpl(String name) {
        this.setName(name);
        this.setCar(INITIAL_CAR);
        this.setCanParticipate(DEFAULT_PARTICIPATION);
        this.setNumberOfWins(INITIAL_WINS);
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.trim().length() < 5) {
            throw new IllegalArgumentException(String.format(INVALID_NAME, name, 5));
        }
        this.name = name;
    }
    private void setCanParticipate(boolean canParticipate) {
        this.canParticipate = canParticipate;
    }

    private void setCar(Car car) {
        this.car = car;
    }

    private void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Car getCar() {
        return this.car;
    }

    @Override
    public int getNumberOfWins() {
        return this.numberOfWins;
    }

    @Override
    public void addCar(Car car) {
        if (car == null) {
            throw new NullPointerException(CAR_INVALID);
        }
        this.car = car;
        this.canParticipate = true;
    }

    @Override
    public void winRace() {
        this.numberOfWins++;
    }

    @Override
    public boolean getCanParticipate() {
        return this.canParticipate;
    }
}
