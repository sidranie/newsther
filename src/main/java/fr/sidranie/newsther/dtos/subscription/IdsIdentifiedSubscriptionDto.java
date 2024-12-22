package fr.sidranie.newsther.dtos.subscription;

public class IdsIdentifiedSubscriptionDto {
    private Long personId;
    private Long newsletterId;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getNewsletterId() {
        return newsletterId;
    }

    public void setNewsletterId(Long newsletterId) {
        this.newsletterId = newsletterId;
    }
}
