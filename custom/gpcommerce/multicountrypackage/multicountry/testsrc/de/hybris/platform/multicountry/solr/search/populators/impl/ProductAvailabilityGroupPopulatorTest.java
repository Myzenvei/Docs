/**
 *
 */
package de.hybris.platform.multicountry.solr.search.populators.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityGroupModel;
import de.hybris.platform.multicountry.services.MulticountryRestrictionService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * @author i303807
 *
 */
@UnitTest
public class ProductAvailabilityGroupPopulatorTest
{
    
    private ProductAvailabilityGroupPopulator productAvailabilityGroupPopulator;
    
    
    @Mock
    private MulticountryRestrictionService multiCountryRestrictionService;
    
    @Mock
    private FacetSearchConfig facetSearchConfig;
    
    @Mock
    private IndexedType indexedType;
    
    @Mock
    private SearchConfig searchConfig;
    
    @Mock
    private ProductAvailabilityGroupModel pag;
    
    
    
    @Before
    public void setUp() throws ImpExException
    {
        
        MockitoAnnotations.initMocks(this);
        final Collection<ProductAvailabilityGroupModel> groups = new ArrayList<ProductAvailabilityGroupModel>();
        
        // mocking all needed classes
        given(multiCountryRestrictionService.getCurrentProductAvailabilityGroup()).willReturn(groups);
        given(searchConfig.getDefaultSortOrder()).willReturn(new ArrayList<String>());
        given(facetSearchConfig.getSearchConfig()).willReturn(searchConfig);
        
        productAvailabilityGroupPopulator = new ProductAvailabilityGroupPopulator();
        productAvailabilityGroupPopulator.setMulticountryRestrictionService(multiCountryRestrictionService);
        
    }
    
    @Test
    public void populateShouldBeEmpty()
    {
        // Prepare source and target data
        final SearchQueryPageableData<SolrSearchQueryData> source = new SearchQueryPageableData<SolrSearchQueryData>();
        final SolrSearchRequest target = new SolrSearchRequest();
        final SearchQuery q = new SearchQuery(facetSearchConfig, indexedType);
        
        target.setSearchQuery(q);
        
        // populate target object
        productAvailabilityGroupPopulator.populate(source, target);
        
        final SearchQuery targetSolrSearchQuery = (SearchQuery) target.getSearchQuery();
        
        final List<FacetValueField> facetValues = targetSolrSearchQuery.getFacetValues();
        
        Assert.assertTrue(facetValues.isEmpty());
    }
    
    @Test
    public void populateShouldNotBeEmpty()
    {
        // Prepare source and target data
        final Collection<ProductAvailabilityGroupModel> groups = new ArrayList<ProductAvailabilityGroupModel>();
        
        given(pag.getId()).willReturn("TestGroup");
        
        groups.add(pag);
        
        given(multiCountryRestrictionService.getCurrentProductAvailabilityGroup()).willReturn(groups);
        
        
        final SearchQueryPageableData<SolrSearchQueryData> source = new SearchQueryPageableData<SolrSearchQueryData>();
        final SolrSearchRequest target = new SolrSearchRequest();
        final SearchQuery q = new SearchQuery(facetSearchConfig, indexedType);
        
        target.setSearchQuery(q);
        
        // populate target object
        productAvailabilityGroupPopulator.populate(source, target);
        
        final SearchQuery targetSolrSearchQuery = (SearchQuery) target.getSearchQuery();
        
        final List<FacetValueField> facetValues = targetSolrSearchQuery.getFacetValues();
        
        
        String testGroupName = null;
        Assert.assertFalse(facetValues.isEmpty());
        
        
        // Testing so the added group exists in the target object
        if (!facetValues.isEmpty())
        {
            final Set<String> values = facetValues.get(0).getValues();
            
            if (values != null && !values.isEmpty())
            {
                testGroupName = values.iterator().next();
            }
        }
        
        Assert.assertEquals("TestGroup", testGroupName);
        
    }
    
    
}