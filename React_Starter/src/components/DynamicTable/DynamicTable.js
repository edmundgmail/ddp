import React, { Component } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

class DynamicTable extends Component {
    constructor(props){
        super(props);
    }

  render() {

     return <table className="table" data={this.props.data} />
  }
}

export default DynamicTable;
