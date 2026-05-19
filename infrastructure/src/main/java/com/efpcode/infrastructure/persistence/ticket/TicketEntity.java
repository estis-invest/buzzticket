package com.efpcode.infrastructure.persistence.ticket;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="ticket")
public class TicketEntity {

    protected TicketEntity(){}

    @Id
    @Column(name = "ticket_id", unique = true, nullable = false)
    private UUID id;


    @Column(name = "ticket_slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "ticket_title", unique = false, nullable = false)
    private String title;

    @Column(name = "ticket_description", unique = false, nullable = false)
    private String description;

    @Column(name = "ticket_status", unique = false, nullable = false)
    private String status;

    @Column(name = "ticket_priority", unique = false, nullable = false)
    private String priority;

    @Column(name = "ticket_created_at", unique = false, nullable = false)
    private Instant createdAt;

    @Column(name = "ticket_updated_at", unique = false, nullable = false)
    private Instant updatedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "ticket_assignee",
            joinColumns = @JoinColumn(name = "ticket_id")
    )
    @Column(name = "user_id", nullable = false)
    private Set<UUID> assignees = new HashSet<>();


    @Column(name = "ticket_reported_by_id", nullable = false)
    private UUID reportedById;


    @Column(name = "ticket_owner_partner_id", nullable = false)
    private UUID ownerPartnerId;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<UUID> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<UUID> assignees) {
        this.assignees = assignees;
    }

    public UUID getReportedById() {
        return reportedById;
    }

    public void setReportedById(UUID reportedById) {
        this.reportedById = reportedById;
    }

    public UUID getOwnerPartnerId() {
        return ownerPartnerId;
    }

    public void setOwnerPartnerId(UUID ownerPartnerId) {
        this.ownerPartnerId = ownerPartnerId;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", assignees=" + assignees +
                ", reportedById=" + reportedById +
                ", ownerPartnerId=" + ownerPartnerId +
                '}';
    }
}
