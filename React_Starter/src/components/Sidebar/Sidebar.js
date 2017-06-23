import React, { Component } from 'react';
import { Link } from 'react-router'

class Sidebar extends Component {

  handleClick(e) {
    e.preventDefault();
    e.target.parentElement.classList.toggle('open');
  }

  activeRoute(routeName) {
    return this.props.location.pathname.indexOf(routeName) > -1 ? 'nav-item nav-dropdown open' : 'nav-item nav-dropdown';
  }

  // secondLevelActive(routeName) {
  //   return this.props.location.pathname.indexOf(routeName) > -1 ? "nav nav-second-level collapse in" : "nav nav-second-level collapse";
  // }

  render() {
    return (

      <div className="sidebar">
        <nav className="sidebar-nav">
          <ul className="nav">
            <li className="nav-item">
              <Link to={'/dashboard'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> Dashboard <span className="badge badge-info">NEW</span></Link>
              <Link to={'/dataexplorer'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> Data Explorer</Link>
              <Link to={'/loadentity'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> Load Entity</Link>
              <Link to={'/datamanipulation'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> Dynamic Reporting</Link>
              <Link to={'/datamanipulation'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> ETL Jobs</Link>
              <Link to={'/datamanipulation'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> Data Ingestion</Link>
              <Link to={'/datamanipulation'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i> User Scala Scripts Manager </Link>
              <Link to={'/datamanipulation'} className="nav-link" activeClassName="active"><i className="icon-speedometer"></i>  User SQL Scripts Manager </Link>
            </li>
            <li className="nav-item nav-dropdown">
              <a className="nav-link nav-dropdown-toggle" href="#" onClick={this.handleClick.bind(this)}><i className="icon-star"></i> Pages</a>
              <ul className="nav-dropdown-items">
                <li className="nav-item">
                  <Link to={'/pages/login'} className="nav-link" activeClassName="active"><i className="icon-star"></i> Login</Link>
                </li>
                <li className="nav-item">
                  <Link to={'/pages/register'} className="nav-link" activeClassName="active"><i className="icon-star"></i> Register</Link>
                </li>
                <li className="nav-item">
                  <Link to={'/pages/404'} className="nav-link" activeClassName="active"><i className="icon-star"></i> Error 404</Link>
                </li>
                <li className="nav-item">
                  <Link to={'/pages/500'} className="nav-link" activeClassName="active"><i className="icon-star"></i> Error 500</Link>
                </li>
              </ul>
            </li>

          </ul>
        </nav>
      </div>
    )
  }
}

export default Sidebar;
