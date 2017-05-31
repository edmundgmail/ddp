import React, { Component } from 'react';
import {Table} from 'reactable';

import Globals from '../Globals';

class LoadEntity extends Component {
    constructor(props) {
        super(props);
        this.state = {
            level: '',
            options: [],
            entityOptions:[],
            dataSource:'',
            dataEntity:'',
            tableData:[],
        };

        this.changeSource=this.changeSource.bind(this);
        this.changeEntity=this.changeEntity.bind(this);
        this.loadEntities=this.loadEntities.bind(this);
        this.onAddFile=this.onAddFile.bind(this);
        this.handleSubmit=this.handleSubmit.bind(this);
    }

    componentDidMount() {
            fetch(Globals.urlHierarchy)
                .then(result=> {
                    if (result.status >= 400) {
                        throw new Error("Bad response from server");
                    }
                    return result.json();
                }).then(r=>{
                if(r.length>0){
                    this.loadEntities(r[0].id);
                    this.setState({options: r.map(s=>( <option key={s.id} value={s.name}>{s.name}</option>)), dataSource:r[0].name});
                }
            });
    }

    loadEntities(id){
        fetch(Globals.urlHierarchy+"?level=datasource&&id="+id)
            .then(result=> {
                if (result.status >= 400) {
                    throw new Error("Bad response from server");
                }
                return result.json();
            }).then(r=>{
            this.setState({entityOptions: r.map(s=>( <option key={s.id} value={s.name}>{s.name}</option>)), dataEntity:r[0].name});
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
                        <div className="col-md-6">
                            <select className="form-control" id="sources" onChange={this.changeSource}>
                                {this.state.options}
                            </select>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label className="col-md-2 form-control-label" htmlFor="text-input">Select Entity</label>
                        <div className="col-md-6">
                            <select className="form-control" id="entities" onChange={this.changeEntity}  >
                                {this.state.entityOptions}
                            </select>
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
