package com.dts.bookies.booksAPI.queries;

public class QueryGenerator {

    private String intitle; //: Returns results where the text following this keyword is found in the title.
    private String inauthor; //: Returns results where the text following this keyword is found in the author.
    private String inpublisher; //: Returns results where the text following this keyword is found in the publisher.
    private String subject; //: Returns results where the text following this keyword is listed in the category list of the volume.
    private String isbn; //: Returns results where the text following this keyword is the ISBN number.
    private String lccn; //: Returns results where the text following this keyword is the Library of Congress Control Number.
    private String oclc; //: Returns results where the text following this keyword is the Online Computer Library Center number.

    public QueryGenerator() {
        setIntitle("");
        setInauthor("");
        setInpublisher("");
        setSubject("");
        setIsbn("");
        setLccn("");
        setOclc("");
    }

    public String genQ() {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("$");
        stringBuilder.append(genAttribute("intitle", getIntitle()));
        stringBuilder.append(genAttribute("inauthor", getInauthor()));
        stringBuilder.append(genAttribute("inpublisher", getInpublisher()));
        stringBuilder.append(genAttribute("subject", getSubject()));
        stringBuilder.append(genAttribute("isbn", getIsbn()));
        stringBuilder.append(genAttribute("lccn", getLccn()));
        stringBuilder.append(genAttribute("oclc", getOclc()));

        String q = stringBuilder.toString();
        return q.substring(0, q.length()-1);
    }

    private String genAttribute(String attributeName, String value) {
        if(!value.trim().equals("")) {
            return attributeName + ":" + value + "+";
        } else {
            return "";
        }
    }

    public String getIntitle() {
        return intitle;
    }

    public void setIntitle(String intitle) {
        this.intitle = intitle;
    }

    public String getInauthor() {
        return inauthor;
    }

    public void setInauthor(String inauthor) {
        this.inauthor = inauthor;
    }

    public String getInpublisher() {
        return inpublisher;
    }

    public void setInpublisher(String inpublisher) {
        this.inpublisher = inpublisher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLccn() {
        return lccn;
    }

    public void setLccn(String lccn) {
        this.lccn = lccn;
    }

    public String getOclc() {
        return oclc;
    }

    public void setOclc(String oclc) {
        this.oclc = oclc;
    }
}
