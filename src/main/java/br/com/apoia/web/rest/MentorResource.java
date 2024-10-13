package br.com.apoia.web.rest;

import br.com.apoia.repository.MentorRepository;
import br.com.apoia.service.MentorService;
import br.com.apoia.service.dto.MentorDTO;
import br.com.apoia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.apoia.domain.Mentor}.
 */
@RestController
@RequestMapping("/api/mentors")
public class MentorResource {

    private static final Logger LOG = LoggerFactory.getLogger(MentorResource.class);

    private static final String ENTITY_NAME = "mentor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MentorService mentorService;

    private final MentorRepository mentorRepository;

    public MentorResource(MentorService mentorService, MentorRepository mentorRepository) {
        this.mentorService = mentorService;
        this.mentorRepository = mentorRepository;
    }

    /**
     * {@code POST  /mentors} : Create a new mentor.
     *
     * @param mentorDTO the mentorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mentorDTO, or with status {@code 400 (Bad Request)} if the mentor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MentorDTO> createMentor(@RequestBody MentorDTO mentorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Mentor : {}", mentorDTO);
        if (mentorDTO.getId() != null) {
            throw new BadRequestAlertException("A new mentor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mentorDTO = mentorService.save(mentorDTO);
        return ResponseEntity.created(new URI("/api/mentors/" + mentorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mentorDTO.getId().toString()))
            .body(mentorDTO);
    }

    /**
     * {@code PUT  /mentors/:id} : Updates an existing mentor.
     *
     * @param id the id of the mentorDTO to save.
     * @param mentorDTO the mentorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentorDTO,
     * or with status {@code 400 (Bad Request)} if the mentorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mentorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MentorDTO> updateMentor(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody MentorDTO mentorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mentor : {}, {}", id, mentorDTO);
        if (mentorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mentorDTO = mentorService.update(mentorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentorDTO.getId().toString()))
            .body(mentorDTO);
    }

    /**
     * {@code PATCH  /mentors/:id} : Partial updates given fields of an existing mentor, field will ignore if it is null
     *
     * @param id the id of the mentorDTO to save.
     * @param mentorDTO the mentorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentorDTO,
     * or with status {@code 400 (Bad Request)} if the mentorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mentorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mentorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MentorDTO> partialUpdateMentor(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody MentorDTO mentorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mentor partially : {}, {}", id, mentorDTO);
        if (mentorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MentorDTO> result = mentorService.partialUpdate(mentorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mentors} : get all the mentors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mentors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MentorDTO>> getAllMentors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Mentors");
        Page<MentorDTO> page = mentorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mentors/:id} : get the "id" mentor.
     *
     * @param id the id of the mentorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mentorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> getMentor(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Mentor : {}", id);
        Optional<MentorDTO> mentorDTO = mentorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mentorDTO);
    }

    /**
     * {@code DELETE  /mentors/:id} : delete the "id" mentor.
     *
     * @param id the id of the mentorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Mentor : {}", id);
        mentorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
