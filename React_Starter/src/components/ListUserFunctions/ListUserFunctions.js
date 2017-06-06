import React, { Component } from 'react';
import {Treebeard,decorators} from 'react-treebeard';

import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import Globals from '../../views/Globals';
import AddNewDataModal from '../AddNewDataModal';
import * as filters from './filter';


var data={name: 'root' , level : 'root', children: [
    {name:'a', level:'owner', children:[
        {name:'b', level: 'report', children : [
            {name: 'report1', level: 'file', content:"this is test"}
            ]}
            ]}]};

var myHeader = (props) => {
    const style = props.style;
    const iconType = props.node.level==='root'? 'empire' : (props.node.level==='owner' ? 'database' : ( props.node.level==='report'? 'table' : 'file-o'));
    const iconClass = `fa fa-${iconType}`;
    const iconStyle = { marginRight: '5px' };
    return (
        <div style={style.base}>
            <div style={style.title}>
                <i className={iconClass} style={iconStyle}/>
                {props.node.name}
            </div>
        </div>
    );
};

class ListUserFunctions extends Component {
    constructor(props){
        super(props);
        this.state= {
            data: data,
            level: '',
            modal:false,
            changed:false
        };
        this.onToggle = this.onToggle.bind(this);
        this.handleNewData=this.handleNewData.bind(this);
        this.toggle = this.toggle.bind(this);    }


    toggle() {
        console.log("toggle");
        this.setState({modal: !this.state.modal});
    }


    handleNewData(e){
        if(e.name && e.name !== ''){
            const node = this.state.cursor;
            if(node.children) node.children.push(e);
            else node.children=[e];
            let children = filters.findDataNode(data, node.level, node.id).children;
            if(children)children.push(e);
            else children=[e];
            this.setState({ cursor: node, changed: true, level : node.level, data:data });

        }
    }

    handleSubmit(){

    }

    onToggle(node, toggled) {
        if (this.state.cursor) {
            this.state.cursor.active = false;
        }
        node.active = true;
        if(node.children){ node.toggled = toggled; }
        this.setState({ cursor: node, level: node.level});
        this.props.uponUpdate(node.content);
  }

  render() {
        decorators.Header=myHeader;
        return (
            <div>
                <Treebeard data={this.state.data} onToggle={this.onToggle} decorators={decorators}/>
                <div className="card-block">
                    <Button onClick={this.toggle}>Launch demo modal</Button>
                    <Modal isOpen={this.state.modal} toggle={this.toggle} className={this.props.className}>
                        <ModalHeader toggle={this.toggle}>Modal title</ModalHeader>
                        <ModalBody>
                            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                        </ModalBody>
                        <ModalFooter>
                            <Button color="primary" onClick={this.toggle}>Do Something</Button>{' '}
                            <Button color="secondary" onClick={this.toggle}>Cancel</Button>
                        </ModalFooter>
                    </Modal>
                </div>
            </div>
  );
  }
}

export default ListUserFunctions;
