package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import ch.heigvd.schoolpulse.TestAuthor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.List;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains automated tests to validate the client and the server
 * implementation of the Roulette Protocol (version 2)
 *
 * @author Maxime Vulliens
 */
public class RouletteV2MaximeVulliensTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Rule
  public EphemeralClientServerPair roulettePair = new EphemeralClientServerPair(RouletteV2Protocol.VERSION);

  @Test
  @TestAuthor(githubId = "Maxime-Vulliens")
  public void theServerShouldHaveZeroStudentsAtStart() throws IOException {
    int port = roulettePair.getServer().getPort();
    IRouletteV2Client client = new RouletteV2ClientImpl();
    client.connect("localhost", port);
    int numberOfStudents = client.getNumberOfStudents();
    assertEquals(0, numberOfStudents);
  }

  // Updated test

  @Test
  @TestAuthor(githubId = "Maxime-Vulliens")
  public void theServerShouldReturnTheCorrectVersionNumber() throws IOException {
    assertEquals(RouletteV2Protocol.VERSION, roulettePair.getClient().getProtocolVersion());
  }


  // New Tests

  // Test "CLEAR"
  @Test
  @TestAuthor(githubId = "Maxime-Vulliens")
  public void theServerShouldClearStudentList() throws IOException, EmptyStoreException {
    IRouletteV2Client client = (IRouletteV2Client)roulettePair.getClient();
    client.loadStudent("Maxime");
    client.loadStudent("Dylan");
    client.loadStudent("Fabien");
    assertEquals(3,client.getNumberOfStudents());
    client.clearDataStore();
    assertEquals(0,client.getNumberOfStudents());
  }

  // Test "LIST"
  @Test
  @TestAuthor(githubId = "Maxime-Vulliens")
  public void theServerShouldListStudent() throws IOException, EmptyStoreException {
    List<Student> test;
    IRouletteV2Client client = (IRouletteV2Client)roulettePair.getClient();
    client.loadStudent("Max");
    client.loadStudent("Dylan");
    client.loadStudent("Yann");
    assertEquals(3,client.getNumberOfStudents());
    test = client.listStudents();
    assertEquals(3, test.size());
  }




}
