import React, { Component } from 'react';
import {Table} from 'reactable';
import Globals from '../Globals';

class LoadEntity extends Component {
    constructor(props) {
        super(props);
        this.state = {
            level: '',
            options: [],
            entityOptions: [],
            dataSource: '',
            dataEntity: '',
            tableData: [],
            showNewSource: false,
            newSourceName: ''
        };

        this.changeSource = this.changeSource.bind(this);
        this.changeEntity = this.changeEntity.bind(this);
        this.loadEntities = this.loadEntities.bind(this);
        this.onAddFile = this.onAddFile.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.toggleNewSource = this.toggleNewSource.bind(this);
        this.handleDataSourceChangeEvent = this.handleDataSourceChangeEvent.bind(this);
        this.createNewSource = this.createNewSource.bind(this);
    }

    createNewEntity(e){
        e.preventDefault();
        if(this.state.newEntityName===""){
            alert("Entity name can't be empty!");
            return;
        }
        else if(this.state.entityOptions.some(v=>this.state.newEntityName===v.key)){
            alert("The name already exists, please use a different name");
            return;
        }
        else{
            this.setState({entityOptions: this.state.entityOptions.concat(<option key={this.state.newEntityName} value={this.state.newEntityName}>{this.state.newEntityName}</option>)});
            this.toggleNewEntity(e);
        }
    }


    handleDataEntityChangeEvent(e) {
        this.setState({newEntityName:e.target.value})
    }

    toggleNewEntity(e){
        e.preventDefault();
        this.setState({showNewEntity: !this.state.showNewEntity, newEntityName:''})
    }



    createNewSource(e){
        e.preventDefault();
        if(this.state.newSourceName===""){
            alert("Source name can't be empty!");
            return;
        }
        else if(this.state.options.some(v=>this.state.newSourceName===v.key)){
            alert("The name already exists, please use a different name");
            return;
        }
        else{
            this.setState({options: this.state.options.concat(<option key={this.state.newSourceName} value={this.state.newSourceName}>{this.state.newSourceName}</option>)});
            this.toggleNewSource(e);
        }
    }


    handleDataSourceChangeEvent(e) {
        this.setState({newSourceName:e.target.value})
    }

    toggleNewSource(e){
        e.preventDefault();
        this.setState({showNewSource: !this.state.showNewSource, newSourceName:''})
    }

    componentDidMount() {
            fetch(Globals.urlHiveHierarchy)
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(res=>{
                 let r = res.result;
                if(r.length>0){
                    this.loadEntities(r[0].databaseName);
                    this.setState({options: r.map(s=>( <option key={s.databaseName} value={s.databaseName}>{s.databaseName}</option>)), dataSource:r[0].databaseName});
                }
            });
    }

    loadEntities(databaseName){
        fetch(Globals.urlHiveHierarchy+"?level=datasource&&name="+databaseName)
            .then(result=> {
                return result.json();
            }).then(res=>{
                let r=res.result;
                if(r && r.length>0){
                    this.setState({entityOptions: r.map(s=>( <option key={s.tableName} value={s.tableName}>{s.tableName}</option>)), dataEntity:r[0].tableName});
                }
                else{
                    this.setState({entityOptions:[]});
                }
        });
    }

    changeEntity(e){
        this.setState({dataEntity:e.target.value})
    }

    changeSource(e){
        let v = this.state.options.filter(o=>o.props.value===e.target.value)[0];
        this.setState({dataSource:v.props.value});
        this.loadEntities(v.key);
    }

    onAddFile(e){
        e.preventDefault();

        let reader = new FileReader();
        let file = e.target.files[0];

        reader.onloadend = () => {
            this.setState({
                file: file,
            });
        }

       reader.readAsDataURL(file)
    }

    handleSubmit(e){
        e.preventDefault();
        var formData = new FormData();
        formData.append('csv', this.state.file, this.state.dataSource + "." + this.state.dataEntity )

        fetch(Globals.urlPostSampleFile, {
            method: 'POST',
            body: formData
        }).then(result=> {
            if (result.status >= 400) {
                throw new Error("Bad response from server");
            }
            return result.json();
        }).then(r=>{
            this.setState({tableData:r.result})
        });
    }



    render(){
      //alert(this.props.params.sourceId);
      //alert(this.props.params.id);
      return (
      <div className="animated fadeIn">
        <div className="row">
            <div className="col-lg-12">
                <form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                    <div className="form-group row">
                        <label className="col-md-2 form-control-label" htmlFor="text-input">Select Source</label>
                        <div className="col-md-4">
                            <select className="form-control" id="sources" onChange={this.changeSource}>
                                {this.state.options}
                            </select>
                        </div>
                        <div className="row">
                            <button type="submit" className="btn" onClick={this.toggleNewSource}>+</button>
                            { this.state.showNewSource ? <div className="row"> <input type="text" placeholder="New DataSource Name"  onChange={this.handleDataSourceChangeEvent}/> <input type="submit" value="Add" onClick={this.createNewSource}/> </div> : null }
                        </div>
                    </div>

                    <div className="form-group row">
                        <label className="col-md-2 form-control-label" htmlFor="text-input">Select Entity</label>
                        <div className="col-md-6">
                            <select className="form-control" id="entities" onChange={this.changeEntity}  >
                                {this.state.entityOptions}
                            </select>
                        </div>
                        <div className="row">
                            <button type="submit" className="btn" onClick={this.toggleNewEntity}>+</button>
                            { this.state.showNewEntity? <div className="row"> <input type="text" placeholder="New DataEntity Name"  onChange={this.handleDataEntityChangeEvent}/> <input type="submit" value="Add" onClick={this.createNewEntity}/> </div> : null }
                        </div>

                    </div>

                    <div className="form-group row">
                        <label className="col-md-2 form-control-label" htmlFor="text-input">Select File</label>
                        <div className="col-md-6">
                                <input type='file' onChange={this.onAddFile}/>
                                <button type="submit" onClick={this.handleSubmit}>Upload File</button>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label className="col-md-2 form-control-label" htmlFor="text-input">Select File Type</label>
                        <div className="col-md-6">
                            <select className="form-control" id="entities">
                                <option>csv</option>
                                <option>xml</option>
                                <option>json</option>
                                <option>copybook</option>
                            </select>
                        </div>
                    </div>

                    <Table
                        className="table"
                        id="table" data={this.state.tableData}
                        noDataText="No matching records found." />

                </form>
            </div>
          </div>
      </div>
    )
  }
}

export default LoadEntity;
