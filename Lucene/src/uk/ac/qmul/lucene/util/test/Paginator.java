/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.qmul.lucene.util.test;

/**
 * Ref: https://hrycan.com/2010/02/10/paginating-lucene-search-results/
 * @author sureshhewapathirana
 */
public class Paginator {
 
    public ArrayLocation calculateArrayLocation(int totalHits, int pageNumber,
        int pageSize) {
        ArrayLocation al = new ArrayLocation();
 
        if (totalHits < 1 || pageNumber < 1 || pageSize < 1) {
            al.setStart(0);
            al.setEnd(0);
            return al;
        }
 
        int start= 1 + (pageNumber -1) * pageSize;
        int end = Math.min(pageNumber * pageSize, totalHits);
        if (start > end) {
            start = Math.max(1, end - pageSize);
        }
 
        al.setStart(start);
        al.setEnd(end);
        return al;
    }
}