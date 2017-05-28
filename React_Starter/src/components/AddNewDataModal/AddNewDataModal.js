import React, { Component } from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

class AddNewDataModal extends Component {
    constructor(props){
        super(props);
        this.state={
            name: '',
            desc:''
        };
        this.toggle = this.toggle.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(name, e) {
        if(name==='name') this.setState({name:e.target.value});
        else if(name=='desc') this.setState({desc:e.target.value});
    }

    onSubmit(){
        const level = (this.props.level==='datasource') ? 'dataentity' : (this.props.level === 'root' ? 'datasource' : null);
        var d = {id: -1, name: this.state.name, description: this.state.desc, level: level};
        this.props.onDataChange(d);
        this.toggle();
    }

    toggle() {
        this.props.onHide()
        this.setState({name:'',desc:''});
    }

  render() {
      const level = (this.props.level==='datasource') ? 'Data Entity' : (this.props.level === 'root' ? 'Data Source' : null);
      if(!level) return null;
      const { active } = this.props;


      return (

        <div>
          <Modal isOpen={this.props.isOpen} toggle={this.toggle} className={this.props.className}>
              <ModalHeader toggle={this.toggle}>Add New {level}</ModalHeader>
            <ModalBody>
                <div className="form-group">
                    <input type="text" className="form-control" id="name"  onChange={this.handleChange.bind(this, 'name')}  value={this.state.name} placeholder='Enter name'/>
                    <input type="text" className="form-control" id="desc"  onChange={this.handleChange.bind(this, 'desc')} value={this.state.desc} placeholder='Enter Description'/>
                </div>
            </ModalBody>
            <ModalFooter>
              <Button color="primary" onClick={this.onSubmit}>Add</Button>{' '}
              <Button color="secondary" onClick={this.toggle}>Cancel</Button>
            </ModalFooter>
          </Modal>
        </div>
    )
  }
}

export default AddNewDataModal;
