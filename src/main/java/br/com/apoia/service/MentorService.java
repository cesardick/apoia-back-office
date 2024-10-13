package br.com.apoia.service;

import br.com.apoia.domain.Mentor;
import br.com.apoia.repository.MentorRepository;
import br.com.apoia.service.dto.MentorDTO;
import br.com.apoia.service.mapper.MentorMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.apoia.domain.Mentor}.
 */
@Service
@Transactional
public class MentorService {

    private static final Logger LOG = LoggerFactory.getLogger(MentorService.class);

    private final MentorRepository mentorRepository;

    private final MentorMapper mentorMapper;

    public MentorService(MentorRepository mentorRepository, MentorMapper mentorMapper) {
        this.mentorRepository = mentorRepository;
        this.mentorMapper = mentorMapper;
    }

    /**
     * Save a mentor.
     *
     * @param mentorDTO the entity to save.
     * @return the persisted entity.
     */
    public MentorDTO save(MentorDTO mentorDTO) {
        LOG.debug("Request to save Mentor : {}", mentorDTO);
        Mentor mentor = mentorMapper.toEntity(mentorDTO);
        mentor = mentorRepository.save(mentor);
        return mentorMapper.toDto(mentor);
    }

    /**
     * Update a mentor.
     *
     * @param mentorDTO the entity to save.
     * @return the persisted entity.
     */
    public MentorDTO update(MentorDTO mentorDTO) {
        LOG.debug("Request to update Mentor : {}", mentorDTO);
        Mentor mentor = mentorMapper.toEntity(mentorDTO);
        mentor = mentorRepository.save(mentor);
        return mentorMapper.toDto(mentor);
    }

    /**
     * Partially update a mentor.
     *
     * @param mentorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MentorDTO> partialUpdate(MentorDTO mentorDTO) {
        LOG.debug("Request to partially update Mentor : {}", mentorDTO);

        return mentorRepository
            .findById(mentorDTO.getId())
            .map(existingMentor -> {
                mentorMapper.partialUpdate(existingMentor, mentorDTO);

                return existingMentor;
            })
            .map(mentorRepository::save)
            .map(mentorMapper::toDto);
    }

    /**
     * Get all the mentors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MentorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Mentors");
        return mentorRepository.findAll(pageable).map(mentorMapper::toDto);
    }

    /**
     * Get one mentor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MentorDTO> findOne(UUID id) {
        LOG.debug("Request to get Mentor : {}", id);
        return mentorRepository.findById(id).map(mentorMapper::toDto);
    }

    /**
     * Delete the mentor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Mentor : {}", id);
        mentorRepository.deleteById(id);
    }
}
