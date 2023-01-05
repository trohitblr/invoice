package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.InvoiceGStLineItem;
import com.retailer.invoice.domain.enumeration.ArticleType;
import com.retailer.invoice.repository.InvoiceGStLineItemRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvoiceGStLineItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceGStLineItemResourceIT {

    private static final ArticleType DEFAULT_ARTICLE_TYPE = ArticleType.GNG;
    private static final ArticleType UPDATED_ARTICLE_TYPE = ArticleType.AIR;

    private static final String DEFAULT_ARTICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HSNSAC = "AAAAAAAAAA";
    private static final String UPDATED_HSNSAC = "BBBBBBBBBB";

    private static final Float DEFAULT_SGST = 1F;
    private static final Float UPDATED_SGST = 2F;

    private static final Float DEFAULT_CGST = 1F;
    private static final Float UPDATED_CGST = 2F;

    private static final Float DEFAULT_SGST_TAX_AMOUNT = 1F;
    private static final Float UPDATED_SGST_TAX_AMOUNT = 2F;

    private static final Float DEFAULT_CGST_TAX_AMOUNT = 1F;
    private static final Float UPDATED_CGST_TAX_AMOUNT = 2F;

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final Float DEFAULT_DISCOUNT = 1F;
    private static final Float UPDATED_DISCOUNT = 2F;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-g-st-line-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{invoiceGStLineItemId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceGStLineItemRepository invoiceGStLineItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceGStLineItemMockMvc;

    private InvoiceGStLineItem invoiceGStLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceGStLineItem createEntity(EntityManager em) {
        InvoiceGStLineItem invoiceGStLineItem = new InvoiceGStLineItem()
            .articleType(DEFAULT_ARTICLE_TYPE)
            .articleId(DEFAULT_ARTICLE_ID)
            .hsnsac(DEFAULT_HSNSAC)
            .sgst(DEFAULT_SGST)
            .cgst(DEFAULT_CGST)
            .sgstTaxAmount(DEFAULT_SGST_TAX_AMOUNT)
            .cgstTaxAmount(DEFAULT_CGST_TAX_AMOUNT)
            .amount(DEFAULT_AMOUNT)
            .discount(DEFAULT_DISCOUNT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return invoiceGStLineItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceGStLineItem createUpdatedEntity(EntityManager em) {
        InvoiceGStLineItem invoiceGStLineItem = new InvoiceGStLineItem()
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .hsnsac(UPDATED_HSNSAC)
            .sgst(UPDATED_SGST)
            .cgst(UPDATED_CGST)
            .sgstTaxAmount(UPDATED_SGST_TAX_AMOUNT)
            .cgstTaxAmount(UPDATED_CGST_TAX_AMOUNT)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return invoiceGStLineItem;
    }

    @BeforeEach
    public void initTest() {
        invoiceGStLineItem = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceGStLineItemRepository.findAll().size();
        // Create the InvoiceGStLineItem
        restInvoiceGStLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceGStLineItem testInvoiceGStLineItem = invoiceGStLineItemList.get(invoiceGStLineItemList.size() - 1);
        assertThat(testInvoiceGStLineItem.getArticleType()).isEqualTo(DEFAULT_ARTICLE_TYPE);
        assertThat(testInvoiceGStLineItem.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testInvoiceGStLineItem.getHsnsac()).isEqualTo(DEFAULT_HSNSAC);
        assertThat(testInvoiceGStLineItem.getSgst()).isEqualTo(DEFAULT_SGST);
        assertThat(testInvoiceGStLineItem.getCgst()).isEqualTo(DEFAULT_CGST);
        assertThat(testInvoiceGStLineItem.getSgstTaxAmount()).isEqualTo(DEFAULT_SGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getCgstTaxAmount()).isEqualTo(DEFAULT_CGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoiceGStLineItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testInvoiceGStLineItem.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvoiceGStLineItem.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInvoiceGStLineItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoiceGStLineItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createInvoiceGStLineItemWithExistingId() throws Exception {
        // Create the InvoiceGStLineItem with an existing ID
        invoiceGStLineItem.setInvoiceGStLineItemId(1L);

        int databaseSizeBeforeCreate = invoiceGStLineItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceGStLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceGStLineItems() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        // Get all the invoiceGStLineItemList
        restInvoiceGStLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=invoiceGStLineItemId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].invoiceGStLineItemId").value(hasItem(invoiceGStLineItem.getInvoiceGStLineItemId().intValue())))
            .andExpect(jsonPath("$.[*].articleType").value(hasItem(DEFAULT_ARTICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].articleId").value(hasItem(DEFAULT_ARTICLE_ID)))
            .andExpect(jsonPath("$.[*].hsnsac").value(hasItem(DEFAULT_HSNSAC)))
            .andExpect(jsonPath("$.[*].sgst").value(hasItem(DEFAULT_SGST.doubleValue())))
            .andExpect(jsonPath("$.[*].cgst").value(hasItem(DEFAULT_CGST.doubleValue())))
            .andExpect(jsonPath("$.[*].sgstTaxAmount").value(hasItem(DEFAULT_SGST_TAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].cgstTaxAmount").value(hasItem(DEFAULT_CGST_TAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getInvoiceGStLineItem() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        // Get the invoiceGStLineItem
        restInvoiceGStLineItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceGStLineItem.getInvoiceGStLineItemId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.invoiceGStLineItemId").value(invoiceGStLineItem.getInvoiceGStLineItemId().intValue()))
            .andExpect(jsonPath("$.articleType").value(DEFAULT_ARTICLE_TYPE.toString()))
            .andExpect(jsonPath("$.articleId").value(DEFAULT_ARTICLE_ID))
            .andExpect(jsonPath("$.hsnsac").value(DEFAULT_HSNSAC))
            .andExpect(jsonPath("$.sgst").value(DEFAULT_SGST.doubleValue()))
            .andExpect(jsonPath("$.cgst").value(DEFAULT_CGST.doubleValue()))
            .andExpect(jsonPath("$.sgstTaxAmount").value(DEFAULT_SGST_TAX_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.cgstTaxAmount").value(DEFAULT_CGST_TAX_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceGStLineItem() throws Exception {
        // Get the invoiceGStLineItem
        restInvoiceGStLineItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceGStLineItem() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();

        // Update the invoiceGStLineItem
        InvoiceGStLineItem updatedInvoiceGStLineItem = invoiceGStLineItemRepository
            .findById(invoiceGStLineItem.getInvoiceGStLineItemId())
            .get();
        // Disconnect from session so that the updates on updatedInvoiceGStLineItem are not directly saved in db
        em.detach(updatedInvoiceGStLineItem);
        updatedInvoiceGStLineItem
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .hsnsac(UPDATED_HSNSAC)
            .sgst(UPDATED_SGST)
            .cgst(UPDATED_CGST)
            .sgstTaxAmount(UPDATED_SGST_TAX_AMOUNT)
            .cgstTaxAmount(UPDATED_CGST_TAX_AMOUNT)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restInvoiceGStLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceGStLineItem.getInvoiceGStLineItemId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceGStLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceGStLineItem testInvoiceGStLineItem = invoiceGStLineItemList.get(invoiceGStLineItemList.size() - 1);
        assertThat(testInvoiceGStLineItem.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
        assertThat(testInvoiceGStLineItem.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testInvoiceGStLineItem.getHsnsac()).isEqualTo(UPDATED_HSNSAC);
        assertThat(testInvoiceGStLineItem.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testInvoiceGStLineItem.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceGStLineItem.getSgstTaxAmount()).isEqualTo(UPDATED_SGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getCgstTaxAmount()).isEqualTo(UPDATED_CGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceGStLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceGStLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceGStLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceGStLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceGStLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceGStLineItem.getInvoiceGStLineItemId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceGStLineItemWithPatch() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();

        // Update the invoiceGStLineItem using partial update
        InvoiceGStLineItem partialUpdatedInvoiceGStLineItem = new InvoiceGStLineItem();
        partialUpdatedInvoiceGStLineItem.setInvoiceGStLineItemId(invoiceGStLineItem.getInvoiceGStLineItemId());

        partialUpdatedInvoiceGStLineItem
            .articleId(UPDATED_ARTICLE_ID)
            .hsnsac(UPDATED_HSNSAC)
            .cgst(UPDATED_CGST)
            .sgstTaxAmount(UPDATED_SGST_TAX_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restInvoiceGStLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceGStLineItem.getInvoiceGStLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceGStLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceGStLineItem testInvoiceGStLineItem = invoiceGStLineItemList.get(invoiceGStLineItemList.size() - 1);
        assertThat(testInvoiceGStLineItem.getArticleType()).isEqualTo(DEFAULT_ARTICLE_TYPE);
        assertThat(testInvoiceGStLineItem.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testInvoiceGStLineItem.getHsnsac()).isEqualTo(UPDATED_HSNSAC);
        assertThat(testInvoiceGStLineItem.getSgst()).isEqualTo(DEFAULT_SGST);
        assertThat(testInvoiceGStLineItem.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceGStLineItem.getSgstTaxAmount()).isEqualTo(UPDATED_SGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getCgstTaxAmount()).isEqualTo(DEFAULT_CGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoiceGStLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceGStLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceGStLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceGStLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceGStLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceGStLineItemWithPatch() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();

        // Update the invoiceGStLineItem using partial update
        InvoiceGStLineItem partialUpdatedInvoiceGStLineItem = new InvoiceGStLineItem();
        partialUpdatedInvoiceGStLineItem.setInvoiceGStLineItemId(invoiceGStLineItem.getInvoiceGStLineItemId());

        partialUpdatedInvoiceGStLineItem
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .hsnsac(UPDATED_HSNSAC)
            .sgst(UPDATED_SGST)
            .cgst(UPDATED_CGST)
            .sgstTaxAmount(UPDATED_SGST_TAX_AMOUNT)
            .cgstTaxAmount(UPDATED_CGST_TAX_AMOUNT)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restInvoiceGStLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceGStLineItem.getInvoiceGStLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceGStLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceGStLineItem testInvoiceGStLineItem = invoiceGStLineItemList.get(invoiceGStLineItemList.size() - 1);
        assertThat(testInvoiceGStLineItem.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
        assertThat(testInvoiceGStLineItem.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testInvoiceGStLineItem.getHsnsac()).isEqualTo(UPDATED_HSNSAC);
        assertThat(testInvoiceGStLineItem.getSgst()).isEqualTo(UPDATED_SGST);
        assertThat(testInvoiceGStLineItem.getCgst()).isEqualTo(UPDATED_CGST);
        assertThat(testInvoiceGStLineItem.getSgstTaxAmount()).isEqualTo(UPDATED_SGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getCgstTaxAmount()).isEqualTo(UPDATED_CGST_TAX_AMOUNT);
        assertThat(testInvoiceGStLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceGStLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceGStLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceGStLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceGStLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceGStLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceGStLineItem.getInvoiceGStLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceGStLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceGStLineItemRepository.findAll().size();
        invoiceGStLineItem.setInvoiceGStLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceGStLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceGStLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceGStLineItem in the database
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceGStLineItem() throws Exception {
        // Initialize the database
        invoiceGStLineItemRepository.saveAndFlush(invoiceGStLineItem);

        int databaseSizeBeforeDelete = invoiceGStLineItemRepository.findAll().size();

        // Delete the invoiceGStLineItem
        restInvoiceGStLineItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceGStLineItem.getInvoiceGStLineItemId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceGStLineItem> invoiceGStLineItemList = invoiceGStLineItemRepository.findAll();
        assertThat(invoiceGStLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
