import React from 'react';
import { Router, Route, IndexRoute, hashHistory } from 'react-router';

// Containers
import Full from './containers/Full/'
// import Simple from './containers/Simple/'

import Dashboard from './views/Dashboard/'
import DataExplorer from './views/DataExplorer/'
import LoadEntity from './views/LoadEntity/'

export default (
  <Router history={hashHistory}>
    <Route path="/" name="Home" component={Full}>
      <IndexRoute component={Dashboard}/>
      <Route path="dashboard" name="Dashboard" component={Dashboard}/>
        <Route path="dataexplorer" name="DataExplorer" component={DataExplorer}/>
        <Route path="loadentity" name="LoadEntity" component={LoadEntity}/>
    </Route>
  </Router>
);
