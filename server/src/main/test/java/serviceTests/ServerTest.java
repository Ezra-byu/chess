//package serviceTests;
//
//import dataaccess.MySqlDataAccess;
//import model.Pet;
//import model.PetType;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import server.PetServer;
//import server.ServerFacade;
//import service.UserService;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class ServerTest {
//    static private Server myServer;
//
//    @BeforeAll
//    static void startServer() throws Exception {
//        myServer.run(0);
//        var url = "http://localhost:" + myServer.port();
//        server = new ServerFacade(url);
//    }
//
//    @AfterAll
//    static void stopServer() {
//        myServer.stop();
//    }
//
//    @BeforeEach
//    void clear(){UserService.clear();};
//
//    @Test
//    void register() {
//        var joe = new Pet(0, "joe", PetType.CAT);
//        var result = assertDoesNotThrow(() -> server.addPet(joe));
//        assertPetEqual(joe, result);
//    }
//
//    @Test
//    void deletePet() throws Exception {
//        var expected = new ArrayList<Pet>();
//        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));
//
//        var joe = server.addPet(new Pet(0, "joe", PetType.CAT));
//        server.deletePet(joe.id());
//
//        var result = assertDoesNotThrow(() -> server.listPets());
//        assertPetCollectionEqual(expected, List.of(result));
//    }
//
//    @Test
//    void listPets() throws Exception {
//        var expected = new ArrayList<Pet>();
//        expected.add(server.addPet(new Pet(0, "joe", PetType.CAT)));
//        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));
//
//        var result = assertDoesNotThrow(() -> server.listPets());
//        assertPetCollectionEqual(expected, List.of(result));
//    }
//
//    private void assertPetEqual(Pet expected, Pet actual) {
//        assertEquals(expected.name(), actual.name());
//        assertEquals(expected.type(), actual.type());
//    }
//
//    private void assertPetCollectionEqual(Collection<Pet> expected, Collection<Pet> actual) {
//        Pet[] actualList = actual.toArray(new Pet[]{});
//        Pet[] expectedList = expected.toArray(new Pet[]{});
//        assertEquals(expectedList.length, actualList.length);
//        for (var i = 0; i < actualList.length; i++) {
//            assertPetEqual(expectedList[i], actualList[i]);
//        }
//    }
//}