import React, { Component } from 'react';
import {Table} from 'reactable';
import Globals from '../Globals/Globals.js';
import AddNewReportModal from '../../components/AddNewReportModal';
import {Treebeard,decorators} from 'react-treebeard';
import Textarea from 'react-textarea-autosize';

var data={name: 'root' , level : 'root', id: 0};


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

class DataManipulation extends React.Component {
    constructor(props) {
        super(props);
        this.state= {
            data: data,
            level: 'root',
            modal:false,
            changed:false,
            newData:null,
            newParent:null,
            content:''
        };
        this.onToggle = this.onToggle.bind(this);
        this.handleNewData=this.handleNewData.bind(this);
        this.toggle = this.toggle.bind(this);
        this.onChange = this.onChange.bind(this);
        this.saveNew = this.saveNew.bind(this);

    }

    componentDidMount() {
        fetch(Globals.urlUserFunctionHierarchy )
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(res=>{
                data.children=res;
        });
    }


    saveNew(){
        if(this.state.textChanged){
            const node = this.state.cursor;
            fetch(Globals.urlUserFunctionHierarchy, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    sessionKey: 123,
                    needPadding: false,
                    parameter: {
                        className : "com.ddp.access.UserScriptParameter",
                        action:"add",
                        level:node.level,
                        name : node.name,
                        id: node.id,
                        parentId: 0,
                        content: this.state.content
                    }
                })
            })
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(r=>{
                this.setState({textChanged:false});
            });
        }
        else if(this.state.changed){
            fetch(Globals.urlUserFunctionHierarchy, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    sessionKey: 123,
                    needPadding: false,
                    parameter: {
                        className : "com.ddp.access.UserScriptParameter",
                        action:"add",
                        level:this.state.newData.level,
                        name : this.state.newData.name,
                        id: this.state.newData.id,
                        parentId: this.state.newParent.id,
                        content: this.state.content
                    }
                })
            })
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(r=>{
                alert(r);
                this.setState({changed:false, newParent:null, newData:null});
            });
        }
    }

    onChange(e){
        if(this.state.level === 'file' && !this.state.changed)
            this.setState({textChanged: true, content: e.target.value});
    }

    toggle() {
        console.log("toggle");
        this.setState({modal: !this.state.modal});
    }


    handleNewData(e){
        if(e && e !== ''){
            const node = this.state.cursor;

            let level = this.state.level==='root'? 'owner' : (this.state.level==='owner'? 'report' : 'file');
            let newData = {name: e, level: level, id: -1};
            if(node.children) node.children.push(newData);
            else node.children=[newData];

            this.setState({ cursor: node, changed: true, level : node.level, data:data, data, newData: newData, newParent:node });

        }
    }
    onToggle(node, toggled) {
        if (this.state.cursor) {
            this.state.cursor.active = false;
        }
        node.active = true;

        if(!node.children) {
            var url = Globals.urlUserFunctionHierarchy+"?level="+node.level+"&&id="+node.id;
            fetch(url)
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(r=>{
                    if(node.level==='file') {
                        if (r.length > 0) {
                            node = r[0];
                            this.setState({content: node.content});
                        }
                    }
                    else
                        node.children=r;
                //filters.findDataNode(data, node.level, node.id).children=r;
            });
        }

        if(node.children){ node.toggled = toggled; }

        this.setState({ cursor: node, level: node.level, content: this.state.level==='file'? node.content : '' });
    }

    removeNode(){
        const node = this.state.cursor;
        let ans = alert("Node level = " ^ node.level ^ ", name=" ^ node.name ^ ", all children will be removed too. Are you sure to proceed the removal?")
        if(ans)
        {

        }
    }


    render(){
        const style = {
            minWidth:'90%',
            minHeight:'100px',
            resize:'none',
            padding:'9px',
            boxSizing:'border-box',
            fontSize:'15px'};
        decorators.Header=myHeader;

      return (
          <div className="animated fadeIn">
              <div className="row">
                  <div className="col-lg-3">
                      <div>
                          <Treebeard data={this.state.data} onToggle={this.onToggle} decorators={decorators}/>
                          <div className="card-block">
                              <button onClick={this.toggle} disabled={this.state.changed || this.state.level==='file'}>Add</button>
                              <button onClick={this.removeNode} disabled={this.state.changed || this.state.level==='root'}>Remove</button>
                              <button disabled={!this.state.changed && !this.state.textChanged} onClick={this.saveNew}>Save</button>
                              <AddNewReportModal level={this.state.level} isOpen={this.state.modal} onHide={this.toggle} onDataChange={this.handleNewData}/>
                          </div>
                      </div>
                  </div>
                  <div className="col-lg-9">
                      <Textarea style={style} value={this.state.content} onChange={this.onChange}/>
                  </div>
              </div>
          </div>
    )
  }
}

export default DataManipulation;
