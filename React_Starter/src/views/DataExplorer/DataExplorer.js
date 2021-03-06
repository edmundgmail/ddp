import React, { Component } from 'react';
import {Treebeard, decorators} from 'react-treebeard';
import { StyleRoot } from 'radium';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

//import data from './data';
import styles from './styles';
import * as filters from './filter';
import Globals from '../Globals/Globals.js';
import AddNewDataModal from '../../components/AddNewDataModal';
import routes from '../../routes';
import { Router, Route, IndexRoute, hashHistory } from 'react-router';


const HELP_MSG = 'Select A Node To See Its Data Structure Here...';
var data={id: 0, name:"root",level:"root", children:null};

// Example: Customising The Header Decorator To Include Icons
var decoratorHeader = (props) => {
    const style = props.style;
    const iconType = props.node.level==='root' ? 'empire' : ( (props.node.level=== 'datasource') ? 'database' : ( props.node.level === 'dataentity' ? 'table' : 'file-o' ) );
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

class NodeViewer extends React.Component {
    constructor(props){
        super(props);
    }

    render(){
        const style = styles.viewer;
        let json = "";
        if(!this.props.node){ json = HELP_MSG; }
        else
            json = this.props.node.description;//JSON.stringify(this.props.node, null, 4);

        return (
            <div style={style.base}>
                {json}
            </div>
        );
    }
}

NodeViewer.propTypes = {
    node: React.PropTypes.object
};

class DataExplorer extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            level: '',
            data:data,
            modal:false,
            changed: false
        };

        this.onToggle = this.onToggle.bind(this);
        this.handleNewData=this.handleNewData.bind(this);
        this.toggle = this.toggle.bind(this);
        this.saveNew=this.saveNew.bind(this);

    }

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

    saveNew(){
        let node=filters.findDataNode(data, null, -1);
        if(node){
            alert("found it");
            fetch(Globals.urlHierarchy, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    sessionKey: 123,
                    needPadding: false,
                    parameter: {
                        className : "com.ddp.access.NewDataSourceParameter",
                        level:node.level,
                        name : node.name,
                        desc: node.description,
                        sourceId: node.level==='datasource'? 0 : node.sourceId
                    }
                })
            })
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(r=>{
                node.id=r.id;
                this.setState({changed:false});
            });
        }
    }

    componentDidMount() {

        fetch(Globals.urlHierarchy)
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(r=>{
                data.children=r;// = {name: 'root', level:'root', children: r};
                this.setState({data: data});
            });
    }

    onToggle(node, toggled){
        if(this.state.cursor){this.state.cursor.active = false;}
        node.active = true;

        if(!node.children && node.level!='datafield') {
            var url = Globals.urlHierarchy+"?level="+node.level+"&&id="+node.id;
            fetch(url)
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(r=>{
                    node.children=r;
                    filters.findDataNode(data, node.level, node.id).children=r;
            });
        }
        if(node.children){ node.toggled = toggled; }
        this.setState({ cursor: node, level : node.level, data:data });
    }
    onFilterMouseUp(e){
        const filter = e.target.value.trim();
        if(!filter){ return this.setState({data}); }
        var filtered = filters.filterTree(data, filter);
        filtered = filters.expandFilteredNodes(filtered, filter);
        this.setState({data: filtered});
    }
    render(){
        decorators.Header=decoratorHeader;

            return (
            <StyleRoot>
                <div className="row">
                <div style={styles.searchBox}>
                    <div className="input-group">
                        <span className="input-group-addon">
                          <i className="fa fa-search"></i>
                        </span>
                        <input type="text"
                               className="form-control"
                               placeholder="Search the tree..."
                               onKeyUp={this.onFilterMouseUp.bind(this)}
                        />
                    </div>

                </div>

                    <div className="card-block">
                        <button type="button" disabled={this.state.changed || (this.state.level!=='root' && this.state.level!=='datasource')} className="btn" onClick={this.toggle}>Add New</button>
                        <button type="button" disabled={!this.state.changed} className="btn" onClick={this.saveNew}>Save</button>
                    </div>
                    <AddNewDataModal level={this.state.level} isOpen={this.state.modal} onDataChange={this.handleNewData} onHide={this.toggle}/>

                </div>
                <div className="row">
                <div style={styles.component}>
                    <Treebeard
                        data={this.state.data}
                        onToggle={this.onToggle}
                        decorators={decorators}
                    />
                </div>

                <div style={styles.component}>
                    <NodeViewer node={this.state.cursor}/>
                </div>

                </div>
            </StyleRoot>

        );
    }
}

export default DataExplorer;
