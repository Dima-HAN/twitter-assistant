package com.twitterassistant.service;

import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SearchResult<T> {

  // Constants ---------------------------------------------------------------------------------------------- Constants

  private static final transient Logger LOG = LoggerFactory.getLogger(SearchResult.class);

  // Instance Variables ---------------------------------------------------------------------------- Instance Variables

  private List<T> results;

  private int total;

  private int firstPage;

  private int maxResults;

  private FullTextQuery query;

  // Constructors ---------------------------------------------------------------------------------------- Constructors

  public SearchResult(FullTextQuery query, int firstPage, int maxResults) {
    this.query = query;
    this.firstPage = firstPage;
    this.maxResults = maxResults;

    total = query.getResultSize();
    results = query.getResultList();
    LOG.debug("Lucene Query Result Found: " + total + " record(s)");
  }

  // Public Methods ------------------------------------------------------------------------------------ Public Methods

  public List<Facet> retrieveFacets(String facetName) {
    FacetManager facetManager = query.getFacetManager();
    return facetManager.getFacets(facetName);
  }

  // Protected Methods ------------------------------------------------------------------------------ Protected Methods

  // Private Methods ---------------------------------------------------------------------------------- Private Methods

  // Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

  public int getTotal() {
    return total;
  }

  public int getFirstPage() {
    return firstPage;
  }

  public int getMaxResults() {
    return maxResults;
  }

  public List<T> getResults() {
    return results;
  }

}
