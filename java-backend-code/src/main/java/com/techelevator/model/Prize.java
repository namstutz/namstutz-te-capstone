package com.techelevator.model;

import java.sql.Timestamp;

public class Prize {

    private int id;
    private int familyId;
    private String prizeName;
    private String description;
    private int milestone;
    private int progressMinutes;
    private boolean forParents;
    private boolean forChildren;
    private int maxPrizes;
    private int claimedPrizes;
    private Timestamp startDate;
    private Timestamp endDate;
    private boolean completed;
    private Timestamp completionDate;
    private boolean isCurrentlyActive;
    private boolean isExpired;

    public Prize() {
    }

    public Prize(int id, int familyId, String prizeName, String description, int milestone, int progressMinutes, boolean forParents, boolean forChildren, int maxPrizes, int claimedPrizes, Timestamp startDate, Timestamp endDate, boolean completed, Timestamp completionDate, boolean isCurrentlyActive, boolean isExpired) {
        this.id = id;
        this.familyId = familyId;
        this.prizeName = prizeName;
        this.description = description;
        this.milestone = milestone;
        this.progressMinutes = progressMinutes;
        this.forParents = forParents;
        this.forChildren = forChildren;
        this.maxPrizes = maxPrizes;
        this.claimedPrizes = claimedPrizes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.completionDate = completionDate;
        this.isCurrentlyActive = isCurrentlyActive;
        this.isExpired = isExpired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMilestone() {
        return milestone;
    }

    public void setMilestone(int milestone) {
        this.milestone = milestone;
    }

    public boolean isForParents() {
        return forParents;
    }

    public void setForParents(boolean forParents) {
        this.forParents = forParents;
    }

    public boolean isForChildren() {
        return forChildren;
    }

    public void setForChildren(boolean forChildren) {
        this.forChildren = forChildren;
    }

    public int getMaxPrizes() {
        return maxPrizes;
    }

    public void setMaxPrizes(int maxPrizes) {
        this.maxPrizes = maxPrizes;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Timestamp completionDate) {
        this.completionDate = completionDate;
    }

    public int getProgressMinutes() {
        return progressMinutes;
    }

    public void setProgressMinutes(int progressMinutes) {
        this.progressMinutes = progressMinutes;
    }

    public boolean isCurrentlyActive() {
        return isCurrentlyActive;
    }

    public void setCurrentlyActive(boolean currentlyActive) {
        isCurrentlyActive = currentlyActive;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public int getClaimedPrizes() {
        return claimedPrizes;
    }

    public void setClaimedPrizes(int claimedPrizes) {
        this.claimedPrizes = claimedPrizes;
    }

    @Override
    public String toString() {
        return "Prize{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", prizeName='" + prizeName + '\'' +
                ", description='" + description + '\'' +
                ", milestone=" + milestone +
                ", progressMinutes=" + progressMinutes +
                ", forParents=" + forParents +
                ", forChildren=" + forChildren +
                ", maxPrizes=" + maxPrizes +
                ", claimedPrizes=" + claimedPrizes +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", completed=" + completed +
                ", completionDate=" + completionDate +
                ", isCurrentlyActive=" + isCurrentlyActive +
                ", isExpired=" + isExpired +
                '}';
    }
}
