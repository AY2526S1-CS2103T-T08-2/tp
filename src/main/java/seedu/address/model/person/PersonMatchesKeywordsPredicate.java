package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s name, tags, phone, or email match the given keywords.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;
    private final String statusKeyword;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;

    /**
     * Constructs a predicate that matches a {@code Person}
     *
     * @param nameKeywords keywords to check against the person's name
     * @param tagKeywords keywords to check against the person's tags
     * @param statusKeyword keyword to check against the person's status
     * @param phoneKeywords keywords to check against the person's phone
     * @param emailKeywords keywords to check against the person's email
     */
    public PersonMatchesKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords,
            String statusKeyword, List<String> phoneKeywords, List<String> emailKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
        this.statusKeyword = statusKeyword;
        this.phoneKeywords = phoneKeywords;
        this.emailKeywords = emailKeywords;
    }

    @Override
    public boolean test(Person person) {
        boolean matchesName = nameKeywords.isEmpty() || nameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));

        boolean matchesTag = tagKeywords.isEmpty() || tagKeywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));

        boolean matchesStatus = statusKeyword == null || statusKeyword.isEmpty()
                || statusKeyword.equalsIgnoreCase(person.getStatus().name());

        boolean matchesPhone = phoneKeywords.isEmpty() || phoneKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getPhone().value, keyword));

        boolean matchesEmail = emailKeywords.isEmpty() || emailKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getEmail().value, keyword));

        return matchesName && matchesTag && matchesStatus && matchesPhone && matchesEmail;
    }

    /**
     * Returns the status keyword used for filtering.
     *
     * @return The status keyword, or null if no status filter is applied.
     */
    public String getStatusKeyword() {
        return statusKeyword;
    }

    /**
     * Returns the tag keywords used for filtering.
     *
     * @return The list of tag keywords.
     */
    public List<String> getTagKeywords() {
        return tagKeywords;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonMatchesKeywordsPredicate)) {
            return false;
        }

        PersonMatchesKeywordsPredicate otherPredicate = (PersonMatchesKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && tagKeywords.equals(otherPredicate.tagKeywords)
                && (statusKeyword == null ? otherPredicate.statusKeyword == null
                    : statusKeyword.equals(otherPredicate.statusKeyword))
                && phoneKeywords.equals(otherPredicate.phoneKeywords)
                && emailKeywords.equals(otherPredicate.emailKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("tagKeywords", tagKeywords)
                .add("statusKeyword", statusKeyword)
                .add("phoneKeywords", phoneKeywords)
                .add("emailKeywords", emailKeywords)
                .toString();
    }

}
