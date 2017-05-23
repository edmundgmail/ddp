import React, { Component } from 'react';
import {Treebeard,decorators} from 'react-treebeard';
import { StyleRoot } from 'radium';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

//import data from './data';
import styles from './styles';
import * as filters from './filter';
import Globals from '../Globals';
import AddNewDataModal from '../../components/AddNewDataModal';

const HELP_MSG = 'Select A Node To See Its Data Structure Here...';
var data={name:"root"};

// Example: Customising The Header Decorator To Include Icons
decorators.Header = (props) => {
    const style = props.style;
    const iconType = props.node.level ? ( (props.node.level== 'datasource') ? 'database' : ( props.node.level == 'dataentity' ? 'table' : 'file-o' ) ):'empire';
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
            level: 'datasource',
            data:data
        };
        this.onToggle = this.onToggle.bind(this);

    }

    componentDidMount() {

        fetch(Globals.urlHierarchy)
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(r=>{
                data = {name: 'root', children: r};
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
            });
        }
        if(node.children){ node.toggled = toggled; }
        this.setState({ cursor: node });
    }
    onFilterMouseUp(e){
        const filter = e.target.value.trim();
        if(!filter){ return this.setState({data}); }
        var filtered = filters.filterTree(data, filter);
        filtered = filters.expandFilteredNodes(filtered, filter);
        this.setState({data: filtered});
    }
    render(){
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
