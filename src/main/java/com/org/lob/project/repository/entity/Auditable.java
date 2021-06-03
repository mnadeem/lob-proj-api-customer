package com.org.lob.project.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(name = "CREATED_BY", columnDefinition = "varchar(25)", updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(name = "CREATE_DATE_TIME", columnDefinition = "timestamp default '2021-06-10 20:47:05.967394'", updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", columnDefinition = "varchar(25)")
    protected String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE_TIME", columnDefinition = "timestamp default '2021-06-10 20:47:05.967394'")
    protected LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
