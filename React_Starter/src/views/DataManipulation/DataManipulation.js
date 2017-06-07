import React, { Component } from 'react';
import {Table} from 'reactable';
import Globals from '../Globals';
import AddNewReportModal from '../../components/AddNewReportModal';
import {Treebeard,decorators} from 'react-treebeard';
import Textarea from 'react-textarea-autosize';

var data={name: 'root' , level : 'root'};


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

class DataManipulation extends Component {
    constructor(props) {
        super(props);
        this.state= {
            data: data,
            level: 'root',
            modal:false,
            changed:false,
            newdata:null,
            content:''
        };
        this.onToggle = this.onToggle.bind(this);
        this.handleNewData=this.handleNewData.bind(this);
        this.toggle = this.toggle.bind(this);
        this.onChange = this.onChange.bind(this);
        this.saveNew = this.saveNew.bind(this);

    }

    componentDidMount() {
        fetch(Globals.urlUserFunctionHierarchy)
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(res=>{
            let r = res.result;
            if(r.length>0){
                data.children=r;
            }
        });
    }


    saveNew(){
        if(this.state.changed){

        }
        this.setState({changed: false, newData: null, textChanged:false});
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
            let newData = {name: e, level: level};
            if(node.children) node.children.push(newData);
            else node.children=[newData];

            this.setState({ cursor: node, changed: true, level : node.level, data:data, data, newdata: newData });

        }
    }
    onToggle(node, toggled) {
        if (this.state.cursor) {
            this.state.cursor.active = false;
        }
        node.active = true;
        if(node.children){ node.toggled = toggled; }

        this.setState({ cursor: node, level: node.level, content: this.state.level==='file'? node.content : '' });
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
                              <button onClick={this.toggle} disabled={this.state.changed || (this.state.level!=='root' && this.state.level!=='owner' && this.state.level!=='report')}>Add</button>
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
