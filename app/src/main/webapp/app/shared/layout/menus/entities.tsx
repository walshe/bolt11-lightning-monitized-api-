import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/invoice">
      Invoice
    </MenuItem>
    <MenuItem icon="asterisk" to="/balance">
      Balance
    </MenuItem>
    <MenuItem icon="asterisk" to="/monetized-api">
      Monetized Api
    </MenuItem>
    <MenuItem icon="asterisk" to="/monetized-api-invocation">
      Monetized Api Invocation
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
