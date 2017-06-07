import React, { Component } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

class AddNewReportModal extends Component {
    constructor(props){
        super(props);
        this.state={
            name: ''
        };
        this.toggle = this.toggle.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(name, e) {
        if(name==='name') this.setState({name:e.target.value});
        else if(name=='desc') this.setState({desc:e.target.value});
    }

    onSubmit(){
        const level = (this.props.level==='root') ? 'New Owner' : (this.props.level === 'owner' ? 'New Report' : 'New File');
        this.props.onDataChange(this.state.name);
        this.toggle();
    }

    toggle() {
        this.props.onHide()
        this.setState({name:'',desc:''});
    }

  render() {
      const level = (this.props.level==='root') ? 'New Owner' : (this.props.level === 'owner' ? 'New Report' : 'New File');
      if(!level) return null;
      const { active } = this.props;


      return (

        <div>
          <Modal isOpen={this.props.isOpen} toggle={this.toggle} className={this.props.className}>
              <ModalHeader toggle={this.toggle}>Add {level}</ModalHeader>
            <ModalBody>
                <div className="form-group">
                    <input type="text" className="form-control" id="name"  onChange={this.handleChange.bind(this, 'name')}  value={this.state.name} placeholder='Enter name'/>
                </div>
            </ModalBody>
            <ModalFooter>
              <Button  onClick={this.onSubmit}>Add</Button>{' '}
              <Button  onClick={this.toggle}>Cancel</Button>
            </ModalFooter>
          </Modal>
        </div>
    )
  }
}

export default AddNewReportModal;
