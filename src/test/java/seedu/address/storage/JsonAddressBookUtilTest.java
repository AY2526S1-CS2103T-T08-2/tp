package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class JsonAddressBookUtilTest {

    @Test
    public void readAddressBookFromJsonString_validJson_success() throws Exception {
        String validJson = "{"
                                   + "\"persons\": ["
                                   + "{"
                                   + "\"name\": \"Alice Pauline\","
                                   + "\"phone\": \"94351253\","
                                   + "\"email\": \"alice@example.com\","
                                   + "\"address\": \"123, Jurong West Ave 6, #08-111\","
                                   + "\"tags\": [\"friends\"]"
                                   + "}"
                                   + "]"
                                   + "}";

        ReadOnlyAddressBook addressBook = JsonAddressBookUtil.readAddressBookFromJsonString(validJson);
        Person currentPerson = addressBook.getPersonList().get(0);
        assertEquals(1, addressBook.getPersonList().size());
        assertEquals("Alice Pauline", currentPerson.getName().toString());
        assertEquals("94351253", currentPerson.getPhone().toString());
        assertEquals("alice@example.com", currentPerson.getEmail().toString());
        assertEquals("123, Jurong West Ave 6, #08-111", currentPerson.getAddress().toString());
        assertTrue(currentPerson.getTags().contains(new Tag("friends")));
    }

    @Test
    public void readAddressBookFromJsonString_invalidJson_throwsDataLoadingException() {
        String invalidJson = "{ not valid json }";
        assertThrows(DataLoadingException.class, () -> JsonAddressBookUtil.readAddressBookFromJsonString(invalidJson));
    }

    @Test
    public void readAddressBookFromJsonString_missingPersonFields_throwsDataLoadingException() {
        String jsonWithMissingFields = "{"
                                               + "\"persons\": ["
                                               + "{"
                                               + "\"name\": \"Alice Pauline\""
                                               + "}"
                                               + "]"
                                               + "}";

        assertThrows(DataLoadingException.class, (
        ) -> JsonAddressBookUtil.readAddressBookFromJsonString(jsonWithMissingFields));
    }

    @Test
    public void readAddressBookFromJsonString_invalidPersonFormat_throwsDataLoadingException() {
        String jsonWithInvalidPerson = "{"
                                               + "\"persons\": ["
                                               + "{"
                                               + "\"name\": \"Alice'Pauline\","
                                               + "\"phone\": \"invalid\","
                                               + "\"email\": \"notanemail\","
                                               + "\"address\": \"123 Street\","
                                               + "\"tags\": []"
                                               + "}"
                                               + "]"
                                               + "}";

        assertThrows(DataLoadingException.class, (
        ) -> JsonAddressBookUtil.readAddressBookFromJsonString(jsonWithInvalidPerson));
    }

    @Test
    public void readAddressBookFromJsonString_emptyJson_success() throws Exception {
        String emptyJson = "{\"persons\": []}";
        ReadOnlyAddressBook addressBook = JsonAddressBookUtil.readAddressBookFromJsonString(emptyJson);

        assertEquals(0, addressBook.getPersonList().size());
    }

    @Test
    public void readAddressBookFromJsonString_multiplePersons_success() throws Exception {
        String multiplePersonsJson = "{"
                                             + "\"persons\": ["
                                             + "{"
                                             + "\"name\": \"Alice Pauline\","
                                             + "\"phone\": \"94351253\","
                                             + "\"email\": \"alice@example.com\","
                                             + "\"address\": \"123, Jurong West Ave 6, #08-111\","
                                             + "\"tags\": [\"friends\"]"
                                             + "},"
                                             + "{"
                                             + "\"name\": \"Hoon Meier\","
                                             + "\"phone\": \"8482424\","
                                             + "\"email\": \"stefan@example.com\","
                                             + "\"address\": \"little india\","
                                             + "\"tags\": []"
                                             + "}"
                                             + "]"
                                             + "}";

        ReadOnlyAddressBook addressBook = JsonAddressBookUtil.readAddressBookFromJsonString(multiplePersonsJson);

        assertEquals(2, addressBook.getPersonList().size());
        assertEquals("Alice Pauline", addressBook.getPersonList().get(0).getName().toString());
        assertEquals("Hoon Meier", addressBook.getPersonList().get(1).getName().toString());
    }

    @Test
    public void readAddressBookFromJsonString_emptyString_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> JsonAddressBookUtil.readAddressBookFromJsonString(""));
    }

    @Test
    public void readAddressBookFromJsonString_null_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> JsonAddressBookUtil.readAddressBookFromJsonString(null));
    }
}
