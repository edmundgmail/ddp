import React,{Component} from 'react';

class Globals  extends React.Component{
      static  urlHierarchy = 'http://192.168.56.102:8082/hierarchy';
      static  urlHiveHierarchy = 'http://192.168.56.102:8082/hiveHierarchy';
      static  urlPostSampleFile = 'http://192.168.56.102:8082/postSampleFiles';
}

export default Globals;
