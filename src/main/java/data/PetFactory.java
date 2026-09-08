package data;

import model.Category;
import model.Pet;
import model.Tag;
import net.datafaker.Faker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class PetFactory {

    private static final Faker faker = new Faker();

    public static Pet randomPet(String status) {
        Pet pet = new Pet();

        pet.setId(ThreadLocalRandom.current().nextLong(100_000_000L, 999_999_999L));
        pet.setName(faker.animal().name());
        pet.setCategory(new Category(faker.number().numberBetween(1, 100), faker.animal().name()));
        pet.setPhotoUrls(List.of(faker.internet().url()));
        pet.setTags(List.of(new Tag(faker.number().numberBetween(1, 100), faker.color().name())));
        pet.setStatus(status);

        return pet;
    }
}