package com.example.demo.Model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Serializable {



    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;



    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

    // Getters y setters


    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
