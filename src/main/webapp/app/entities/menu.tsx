import React from 'react';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/mentor">
        <Translate contentKey="global.menu.entities.mentor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/area">
        <Translate contentKey="global.menu.entities.area" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/course">
        <Translate contentKey="global.menu.entities.course" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
