package easterRaces.repositories;

import easterRaces.entities.racers.Race;
import easterRaces.repositories.interfaces.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RaceRepository implements Repository<Race> {

    private Collection<Race> models;

    public RaceRepository() {
        this.models = new ArrayList<>();
    }

    @Override
    public Race getByName(String name) {
        return this.models.stream().filter(m->m.getName().equals(name))
                .limit(1)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Race> getAll() {
        return Collections.unmodifiableCollection(this.models);
    }

    @Override
    public void add(Race model) {
        this.models.add(model);
    }

    @Override
    public boolean remove(Race model) {
        return this.models.removeIf(e->e.getName().equals(model.getName()));
    }
}
