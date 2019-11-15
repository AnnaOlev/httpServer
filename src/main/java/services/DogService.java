package services;

import dao.DogDao;
import models.Dog;

import java.util.List;

public class DogService {

    private DogDao dogDao = new DogDao();

    public DogService() {
    }

    public Dog findDog(int id) {
        return dogDao.findById(id);
    }

    public void saveDog (Dog dogDB) {
        dogDao.save(dogDB);
    }

    public void updateDog (Dog dogDB) {
        dogDao.update(dogDB);
    }

    public void deleteDog (Dog dogDB) {
        dogDao.delete(dogDB);
    }

    public List<Dog> findAllDogs() {
        return dogDao.findAll();
    }

}
