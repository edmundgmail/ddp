import React, { Component } from 'react';
import {Table} from 'reactable';
import Globals from '../Globals';
import ListUserFunctions from '../../components/ListUserFunctions';
import Textarea from 'react-textarea-autosize';

class DataManipulation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            content:''
        };
        this.updateListUserFunction=this.updateListUserFunction.bind(this);
    }

    updateListUserFunction(e){
        this.setState({content: e});
    }

    render(){
        const style = {
            minWidth:'90%',
            minHeight:'100px',
            resize:'none',
            padding:'9px',
            boxSizing:'border-box',
            fontSize:'15px'};

      return (
          <div className="animated fadeIn">
              <div className="row">
                  <div className="col-lg-3">
                      <ListUserFunctions uponUpdate={this.updateListUserFunction}/>
                  </div>
                  <div className="col-lg-9">
                      <Textarea style={style} value={this.state.content}/>
                  </div>
              </div>
          </div>
    )
  }
}

export default DataManipulation;
