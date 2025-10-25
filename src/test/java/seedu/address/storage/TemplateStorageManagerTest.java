package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Status;

public class TemplateStorageManagerTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void readTemplate_nonExistentFile_returnsDefaultTemplate() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String result = storage.readTemplate(Status.CONTACTED);
        // Should return default template
        assertEquals("Template for Contacted contacts", result);
    }

    @Test
    public void readTemplate_existingFile_returnsContent() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String expectedContent = "Test template content";
        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        Files.writeString(templateFile, expectedContent);

        String result = storage.readTemplate(Status.CONTACTED);
        assertEquals(expectedContent, result);
    }

    @Test
    public void saveTemplate_newFile_createsFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String content = "New template";

        storage.saveTemplate(Status.CONTACTED, content);

        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        assertTrue(Files.exists(templateFile));
        assertEquals(content, Files.readString(templateFile));
    }

    @Test
    public void saveTemplate_existingFile_overwritesFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        Path templateFile = temporaryFolder.resolve("contactedTemplate.txt");
        Files.writeString(templateFile, "Old content");

        String newContent = "Updated content";
        storage.saveTemplate(Status.CONTACTED, newContent);

        assertEquals(newContent, Files.readString(templateFile));
    }

    @Test
    public void getDefaultTemplate_contacted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.CONTACTED);
        assertEquals("Template for Contacted contacts", template);
    }

    @Test
    public void getDefaultTemplate_uncontacted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.UNCONTACTED);
        assertEquals("Template for Uncontacted contacts", template);
    }

    @Test
    public void getDefaultTemplate_rejected_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.REJECTED);
        assertEquals("Template for Rejected contacts", template);
    }

    @Test
    public void getDefaultTemplate_accepted_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.ACCEPTED);
        assertEquals("Template for Accepted contacts", template);
    }

    @Test
    public void getDefaultTemplate_unreachable_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.UNREACHABLE);
        assertEquals("Template for Unreachable contacts", template);
    }

    @Test
    public void getDefaultTemplate_busy_returnsCorrectTemplate() {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String template = storage.getDefaultTemplate(Status.BUSY);
        assertEquals("Template for Busy contacts", template);
    }

    @Test
    public void saveAndReadTemplate_multipleStatuses_worksCorrectly() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);

        storage.saveTemplate(Status.CONTACTED, "Contacted template");
        storage.saveTemplate(Status.REJECTED, "Rejected template");
        storage.saveTemplate(Status.ACCEPTED, "Accepted template");

        assertEquals("Contacted template", storage.readTemplate(Status.CONTACTED));
        assertEquals("Rejected template", storage.readTemplate(Status.REJECTED));
        assertEquals("Accepted template", storage.readTemplate(Status.ACCEPTED));
    }

    @Test
    public void saveTemplate_emptyContent_savesEmptyFile() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);

        storage.saveTemplate(Status.CONTACTED, "");

        String result = storage.readTemplate(Status.CONTACTED);
        assertEquals("", result);
    }

    @Test
    public void saveTemplate_multilineContent_preservesNewlines() throws IOException {
        TemplateStorageManager storage = new TemplateStorageManager(temporaryFolder);
        String multilineContent = "Line 1\nLine 2\nLine 3";

        storage.saveTemplate(Status.CONTACTED, multilineContent);

        String result = storage.readTemplate(Status.CONTACTED);
        assertEquals(multilineContent, result);
    }

    /**
     * A Storage stub that stores templates in memory for testing purposes.
     */
    public static class StorageStub implements Storage {
        private final Map<Status, String> templates = new HashMap<>();

        // TemplateStorage methods
        @Override
        public Path getTemplateDirectoryPath() {
            return null; // Not used in tests
        }

        @Override
        public String readTemplate(Status status) throws IOException {
            String template = templates.get(status);
            return template != null ? template : getDefaultTemplate(status);
        }

        @Override
        public void saveTemplate(Status status, String content) throws IOException {
            templates.put(status, content);
        }

        @Override
        public String getDefaultTemplate(Status status) {
            return "Default template for " + status;
        }

        public String getSavedTemplate(Status status) {
            return templates.get(status);
        }

        // AddressBookStorage methods (stubs)
        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            // Do nothing
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            // Do nothing
        }

        // UserPrefsStorage methods (stubs)
        @Override
        public Path getUserPrefsFilePath() {
            return null;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            // Do nothing
        }
    }
}
