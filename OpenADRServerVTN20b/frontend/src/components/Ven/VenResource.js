import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import Typography from '@material-ui/core/Typography';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import AssessmentIcon from '@material-ui/icons/Assessment';
import FolderIcon from '@material-ui/icons/Folder';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Toolbar from '@material-ui/core/Toolbar';
import FilterListIcon from '@material-ui/icons/FilterList';

import FilterPanel from '../common/FilterPanel' 

var constructRoot = (resource, parentId) => {
  var visitedNode = {};
  var venTreeIndex = [];
  for(var i in resource) {
    var res = resource[i];

    if(!visitedNode[res.id]) {
      visitedNode[res.id] = {
        resource: res,
        children: []
      }
    } else {
      visitedNode[res.id] = {
        resource: resource,
        children: visitedNode[res.id].children
      }
    }
    if(res.parent) {

      if(visitedNode[res.parent.id]) {
        var children = visitedNode[res.parent.id].children;
        children.push(res.id)
        visitedNode[res.parent.id].children = children
      } else if(parentId && res.parent.id === parentId) {
        venTreeIndex.push(visitedNode[res.id]);
      }

    } else {
      venTreeIndex.push(visitedNode[res.id]);

    }
    

  }
  
  

  

  function recursiveChildren(node) {

    var newChildrens = [];
    for(var i in node.children) {
      var children = node.children[i];

      var newChildren = recursiveChildren(visitedNode[children]);
      newChildrens.push(newChildren)
    }
    node.children = newChildrens;
    return node;
  }

  var venTree = [];
  for(var j in venTreeIndex) {

    var node= recursiveChildren(venTreeIndex[j]);
    venTree.push(node);
  }
  return venTree;
}

const useTreeItemStyles = makeStyles((theme) => ({
  root: {
    color: theme.palette.text.secondary,
  },
  content: {
    color: theme.palette.text.secondary,
    borderTopRightRadius: theme.spacing(2),
    borderBottomRightRadius: theme.spacing(2),
    paddingRight: theme.spacing(1),
    fontWeight: theme.typography.fontWeightMedium,

  },
  group: {
    marginLeft: 0,
    '& $content': {
      paddingLeft: theme.spacing(2),
    },
  },
  expanded: {},
  selected: {},
  label: {
    fontWeight: 'inherit',
    color: 'inherit',
  },
  labelRoot: {
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0.5, 0),
  },
  labelIcon: {
    marginRight: theme.spacing(1),
  },
  labelText: {
    fontWeight: 'inherit',
    flexGrow: 1,
  },
}));

function StyledTreeItem(props) {
  const classes = useTreeItemStyles();
  const { labelText, labelIcon: LabelIcon, labelInfo, color, bgColor, index, ...other } = props;

  var left = (index > 1) ? 5 * index - 1 : 0;
  
  return (
    <div style={{marginLeft: left}}>
    <TreeItem
     
      key={other.nodeId}
      label={
        <div className={classes.labelRoot} >
          <LabelIcon color="inherit" className={classes.labelIcon} />
          <Typography variant="body2" className={classes.labelText}  >
            {labelText}
          </Typography>
          <Typography variant="caption" color="inherit">
            {labelInfo}
          </Typography>
        </div>
      }
      style={{
        '--tree-view-color': color,
        '--tree-view-bg-color': bgColor,
      }}
      classes={{
        root: classes.root,
        content: classes.content,
        expanded: classes.expanded,
        selected: classes.selected,
        group: classes.group,
        label: classes.label,
      }}
      {...other}
    />
    </div>
  );
}

StyledTreeItem.propTypes = {
  bgColor: PropTypes.string,
  color: PropTypes.string,
  labelIcon: PropTypes.elementType.isRequired,
  labelInfo: PropTypes.string,
  labelText: PropTypes.string.isRequired,
};

export class VenResource extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
    this.state.expanded = [];
    this.state.tree = null;
    this.state.data = {};
    this.state.filters = null;
    this.state.displayFilters = false;

  }

  
  static getDerivedStateFromProps(props, state) {

    var changed = false;
    if (props.filters !== state.filters && props.resource && props.resource.length > 0) {
      /*
      var expanded = [];
      for(var i in props.resource) {
        var node = props.resource[i];
        if(node.type === "VEN") {

          expanded.push(node.id.toString());

        } else if(node.type === "REPORT") {

          expanded.push(node.id.toString());

        } else if(node.type === "RESOURCE") {

          expanded.push(node.id.toString());

        } 
      }
      if(state.expanded && state.expanded.length == 0) {
        state.expanded= expanded
        changed = true;
      }

      */
      state.tree = constructRoot(props.resource)
      
      
    }

    if(!state.data[props.dataId] && props.data && props.dataId) {
      console.log(props.data)
      state.data[props.dataId] = constructRoot(props.data, props.dataId);
      console.log(state.data[props.dataId])
      changed = true;
    }
    if(changed) {
      return state;
    }

    // Return null if the state hasn't changed
    return null;
  }


  onClick = (node, nodeId) => {

      return (e) => {
        //console.log(nodeId)
        var expanded = this.state.expanded
        var data = this.state.data;
        if(expanded.includes(nodeId)) {
          delete data[node.id]
          expanded = expanded.filter(function(value, index, arr){ 
              return value !== nodeId;
          });
        } else {
          expanded.push(nodeId);
        }
        this.setState({expanded})
        console.log(node)
        if((node.type === "FLATTENED" || node.type === "ENDDEVICEASSET" || node.type === "VEN") && !node.children) {
          this.props.resourceActions.searchResourceData(node.venResourceId, node.id);
        }
     }
  }

  handleDisplayFilters = () => {
    this.setState({displayFilters: !this.state.displayFilters})
  }

  render() {
    const {classes} = this.props;
    const {expanded, tree, data, displayFilters} = this.state;
    var that = this;

    function flatten(t) {
      if(t) {
        var newVen = []
        for(var i in t) {
          var newChildren = [];
          var ven = t[i];
          if(ven.children){
            for(var j in ven.children) {
              var report = ven.children[j];
              if(report.children) {
                for(var k in report.children) {
                  var resource = report.children[k];
                  if(resource.children) {
                    for(var l in resource.children) {
                      var endDeviceAsset = resource.children[l];
                      console.log(endDeviceAsset)
                      if(endDeviceAsset.children && endDeviceAsset.children.length > 0) {
                        for(var m in endDeviceAsset.children) {
                          var description = endDeviceAsset.children[m];
                          var label = report.resource.venResourceLabel; 
                          if(resource.resource.venResourceLabel !== "NotSpecified") {
                            label += " / " + resource.resource.venResourceLabel
                          }
                          if(endDeviceAsset.resource.venResourceLabel !== "NotSpecified") {
                            label += " / " + endDeviceAsset.resource.venResourceLabel
                          }
                          label += " / " + description.resource.venResourceLabel

                          newChildren.push({
                            resource: {
                              id: description.resource.id,
                              name: null,
                              parent: ven.resource.id,
                              type: "FLATTENED",
                              venResourceId: {
                                report: report.resource.venResourceId,
                                resource: resource.resource.venResourceId,
                                endDeviceAsset: endDeviceAsset.resource.venResourceId,
                                description: description.resource.venResourceId
                              },
                              venResourceLabel: label
                               }
                          })
                        }
                      } 
                   
                    }
                  }
                }
              }
            }
          }
          newVen.push({
            resource: ven.resource,
            children: newChildren
          })
        }
        return newVen;
      }
      return tree;
      
    }

   
    // var flattenedTree = tree
    function recursiveData(node) {
      var newChildrens = [];
      if(data[node.resource.id]) {

        for(var i in data[node.resource.id]) {

          newChildrens.push(data[node.resource.id][i])
        }

      } else if(node.children) {
        for(var j in node.children) {
          var children = node.children[j];

          var newChildren = recursiveData(children);
          newChildrens.push(newChildren)
        }
      }

      node.children = newChildrens;
      return node;
    }

    var treeWithData = [];
    for(var i in tree) {

      var node= recursiveData(tree[i]);
      treeWithData.push(node);
    }

    treeWithData = flatten(treeWithData)


    function recursiveView(node, index) {
      var ico;
      if(node.resource.type === "VEN") {

        ico = SettingsInputComponentIcon;

      } else if(node.resource.type === "REPORT" ) {

        ico = AssessmentIcon;

      } else if(node.resource.type === "RESOURCE") {

        ico = FolderIcon;

      } else if(node.resource.type === "ENDDEVICEASSET") {

        ico = PowerSettingsNewIcon;

      } else if(node.resource.type === "REPORT_DESCRIPTION") {

        ico = TrendingUpIcon;

      } else if(node.resource.type === "FLATTENED") {
        /*
        public static final String THERMOSTAT = "Thermostat";
        public static final String BASEBOARD_HEATER = "Baseboard_Heater";
        public static final String STRIP_HEATER = "Strip_Heater";
        public static final String WATER_HEATER = "Water_Heater";
        public static final String POOL_PUMP = "Pool_Pump";
        public static final String SAUNA = "Sauna";
        public static final String HOT_TUB = "Hot_tub";
        public static final String SMART_APPLIANCE = "Smart_Appliance";
        public static final String IRRIGATION_PUMP = "Irrigation_Pump";
        public static final String MANAGED_COMMERCIAL_AND_INDUSTRIAL_LOADS = "Managed_Commercial_and_Industrial_Loads";
        public static final String SIMPLE_RESIDENTIAL_ON_OFF_LOADS = "Simple_Residential_On_Off_Loads";
        public static final String EXTERIOR_LIGHTING = "Exterior_Lighting";
        public static final String INTERIOR_LIGHTING = "Interior_Lighting";
        public static final String ELECTRIC_VEHICLE = "Electric_Vehicle";
        public static final String GENERATION_SYSTEMS = "Generation_Systems";
        public static final String LOAD_CONTROL_SWITCH = "Load_Control_Switch";
        public static final String SMART_INVERTER = "Smart_Inverter";
        public static final String EVSE = "EVSE";
        public static final String RESU = "RESU";
        public static final String ENERGY_MANAGEMENT_SYSTEM = "Energy_Management_System";
        public static final String SMART_ENERGYMODULE = "Smart_Energy_Module";
        public static final String STORAGE = "Storage";
        */
        ico = TrendingUpIcon;
        switch(node.resource.venResourceId.endDeviceAsset) {
          case "Thermostat":
            ico = PowerSettingsNewIcon;
            break;
           case "Smart_Energy_Module":
             ico = PowerSettingsNewIcon;
            break;
           default:
             break;
        }

        return <StyledTreeItem key={node.resource.id.toString()}  
                                nodeId={node.resource.id.toString()} 
                                index={index} 
                                labelText={node.resource.venResourceLabel} 
                                labelIcon={ico} 
                                onClick={that.onClick(node.resource, node.resource.id.toString())}/>

      }

      var i = ++index;
      
      if(node.children && node.children.length > 0) {

          return (
            <StyledTreeItem  key={node.resource.id.toString()}  
                              nodeId={node.resource.id.toString()} 
                               index={index} 
                               labelText={node.resource.venResourceLabel} 
                               labelInfo={node.resource.type === "VEN" ? node.resource.reportDescriptionCount.toString() : null}

                               labelIcon={ico} 
                               onClick={that.onClick(node.resource, node.resource.id.toString())}>
              
              {node.children && node.children.map(n => recursiveView(n, i))}
            </StyledTreeItem>
          );
      } 
      else {
        return <StyledTreeItem key={node.resource.id.toString()}  
                                nodeId={node.resource.id.toString()} 
                                index={index} 
                                labelText={node.resource.venResourceLabel} 
                                labelIcon={ico} 
                                labelInfo={node.resource.type === "VEN" ? node.resource.reportDescriptionCount.toString() : null}
                                onClick={that.onClick(node.resource, node.resource.id.toString())}/>
      }
    }



    return (
    <div className={ classes.root }>
    <Toolbar>
      <div style={{flex: '0 0 auto'}}>
        <Typography variant="h6" id="tableTitle">
            Resources
          </Typography>
      </div>
      <div style={{flex: '1 1 100%'}}></div>
      <div style={{flex: '0 0 auto'}}></div>
      <Tooltip title="Filter" onClick={ this.handleDisplayFilters }>
        <IconButton aria-label="Filter">
          <FilterListIcon />
        </IconButton>
      </Tooltip>
    </Toolbar>
    
    {displayFilters ? <div className={classes.tableWrapper} style={{margin: "0px 20px"}}>
    <FilterPanel classes={classes}  type="VEN" hasFilter={{group:true, marketContext:true, venStatus:true, event:true}} 
            group={this.props.group}
            marketContext={this.props.marketContext}
            filter={this.props.filters}
            onFilterChange={this.props.onFilterChange}

            event={this.props.event}
            onEventSuggestionsFetchRequested={this.props.onEventSuggestionsFetchRequested}
            onEventSuggestionsClearRequested={this.props.onEventSuggestionsClearRequested}
            onEventSuggestionsSelect={this.props.onEventSuggestionsSelect}/></div>  : null }


<TreeView style={{margin: "0 20px"}}
      className={classes.root}
      expanded={expanded}
      defaultCollapseIcon={<ArrowDropDownIcon />}
      defaultExpandIcon={<ArrowRightIcon />}
      defaultEndIcon={<div style={{ width: 24 }} />}
>

      {treeWithData && treeWithData.map(n => recursiveView(n, 0))}

      </TreeView>
      <div style={{marginBottom: "20px"}}/>
    </div>
    );
  }
}

export default VenResource;
