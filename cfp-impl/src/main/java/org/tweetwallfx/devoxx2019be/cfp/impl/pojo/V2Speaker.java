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

public class V2Speaker {

    private int id;
    private String firstName;
    private String lastName;
    private String bio;
    private String company;
    private String imageUrl;
    private String twitterHandle;
    private List<V2Proposal> proposals = new ArrayList<V2Proposal>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public List<V2Proposal> getProposals() {
        return null == proposals
                ? Collections.emptyList()
                : Collections.unmodifiableList(proposals);
    }

    public void setProposals(List<V2Proposal> proposals) {
        this.proposals = proposals;
    }

    @Override
    public String toString() {
        return createToString(this, mapOf(
                mapEntry("id", getId()),
                mapEntry("firstName", getFirstName()),
                mapEntry("lastName", getLastName()),
                mapEntry("bio", getBio()),
                mapEntry("company", getCompany()),
                mapEntry("imageUrl", getImageUrl()),
                mapEntry("twitterHandle", getTwitterHandle()),
                mapEntry("proposals", getProposals())
        ), super.toString());
    }
}
