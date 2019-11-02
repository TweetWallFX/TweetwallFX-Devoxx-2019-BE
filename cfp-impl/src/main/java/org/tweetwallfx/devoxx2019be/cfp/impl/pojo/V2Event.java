/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 TweetWallFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tweetwallfx.devoxx2019be.cfp.impl.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.tweetwallfx.util.ToString.createToString;
import static org.tweetwallfx.util.ToString.mapOf;
import static org.tweetwallfx.util.ToString.mapEntry;

public class V2Event {

    private int id;
    private String slug;
    private String name;
    private String description;
    private String codeOfConduct;
    private String website;
    private String twitterHandle;
    private String fromDate;
    private String toDate;
    private String flickrURL;
    private String youTubeURL;
    private boolean live;
    private String theme;
    private Object myBadgeActive;
    private String cfpOpening;
    private String cfpClosing;
    private String eventImageURL;
    private int maxProposals;
    private List<Object> languages = new ArrayList<Object>();
    private List<V2SessionType> sessionTypes = new ArrayList<V2SessionType>();
    private List<V2Track> tracks = new ArrayList<V2Track>();
    private int locationId;
    private String locationName;
    private String locationAddress;
    private String locationCity;
    private String locationCountry;
    private String timezone;
    private double venueLongitude;
    private double venueLatitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeOfConduct() {
        return codeOfConduct;
    }

    public void setCodeOfConduct(String codeOfConduct) {
        this.codeOfConduct = codeOfConduct;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFlickrURL() {
        return flickrURL;
    }

    public void setFlickrURL(String flickrURL) {
        this.flickrURL = flickrURL;
    }

    public String getYouTubeURL() {
        return youTubeURL;
    }

    public void setYouTubeURL(String youTubeURL) {
        this.youTubeURL = youTubeURL;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Object getMyBadgeActive() {
        return myBadgeActive;
    }

    public void setMyBadgeActive(Object myBadgeActive) {
        this.myBadgeActive = myBadgeActive;
    }

    public String getCfpOpening() {
        return cfpOpening;
    }

    public void setCfpOpening(String cfpOpening) {
        this.cfpOpening = cfpOpening;
    }

    public String getCfpClosing() {
        return cfpClosing;
    }

    public void setCfpClosing(String cfpClosing) {
        this.cfpClosing = cfpClosing;
    }

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
    }

    public int getMaxProposals() {
        return maxProposals;
    }

    public void setMaxProposals(int maxProposals) {
        this.maxProposals = maxProposals;
    }

    public List<Object> getLanguages() {
        return null == languages
                ? Collections.emptyList()
                : Collections.unmodifiableList(languages);
    }

    public void setLanguages(List<Object> languages) {
        this.languages = languages;
    }

    public List<V2SessionType> getSessionTypes() {
        return null == sessionTypes
                ? Collections.emptyList()
                : Collections.unmodifiableList(sessionTypes);
    }

    public void setSessionTypes(List<V2SessionType> sessionTypes) {
        this.sessionTypes = sessionTypes;
    }

    public List<V2Track> getTracks() {
        return null == tracks
                ? Collections.emptyList()
                : Collections.unmodifiableList(tracks);
    }

    public void setTracks(List<V2Track> tracks) {
        this.tracks = tracks;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getVenueLongitude() {
        return venueLongitude;
    }

    public void setVenueLongitude(double venueLongitude) {
        this.venueLongitude = venueLongitude;
    }

    public double getVenueLatitude() {
        return venueLatitude;
    }

    public void setVenueLatitude(double venueLatitude) {
        this.venueLatitude = venueLatitude;
    }

    @Override
    public String toString() {
        return createToString(this, mapOf(
                mapEntry("id", getId()),
                mapEntry("slug", getSlug()),
                mapEntry("name", getName()),
                mapEntry("description", getDescription()),
                mapEntry("codeOfConduct", getCodeOfConduct()),
                mapEntry("website", getWebsite()),
                mapEntry("twitterHandle", getTwitterHandle()),
                mapEntry("fromDate", getFromDate()),
                mapEntry("toDate", getToDate()),
                mapEntry("flickrURL", getFlickrURL()),
                mapEntry("youTubeURL", getYouTubeURL()),
                mapEntry("live", isLive()),
                mapEntry("theme", getTheme()),
                mapEntry("myBadgeActive", getMyBadgeActive()),
                mapEntry("cfpOpening", getCfpOpening()),
                mapEntry("cfpClosing", getCfpClosing()),
                mapEntry("eventImageURL", getEventImageURL()),
                mapEntry("maxProposals", getMaxProposals()),
                mapEntry("languages", getLanguages()),
                mapEntry("sessionTypes", getSessionTypes()),
                mapEntry("tracks", getTracks()),
                mapEntry("locationId", getLocationId()),
                mapEntry("locationName", getLocationName()),
                mapEntry("locationAddress", getLocationAddress()),
                mapEntry("locationCity", getLocationCity()),
                mapEntry("locationCountry", getLocationCountry()),
                mapEntry("timezone", getTimezone()),
                mapEntry("venueLongitude", getVenueLongitude()),
                mapEntry("venueLatitude", getVenueLatitude())
        ), super.toString());
    }
}
