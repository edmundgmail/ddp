import React, { Component } from 'react';
import Globals from '../Globals'


class Dashboard extends React.Component {
    constructor(props) {
        super(props);

        this.state = {

        };
    }

    render() {
        return (
        <div>{Globals.urlHierarchy}</div>
        );
    }
}

module.exports = Dashboard;
