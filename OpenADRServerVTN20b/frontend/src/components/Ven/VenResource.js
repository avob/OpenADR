import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import Typography from '@material-ui/core/Typography';
import MailIcon from '@material-ui/icons/Mail';
import DeleteIcon from '@material-ui/icons/Delete';
import Label from '@material-ui/icons/Label';
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import InfoIcon from '@material-ui/icons/Info';
import ForumIcon from '@material-ui/icons/Forum';
import LocalOfferIcon from '@material-ui/icons/LocalOffer';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';

import SettingsInputComponentIcon from '@material-ui/icons/SettingsInputComponent';
import AssessmentIcon from '@material-ui/icons/Assessment';
import FolderIcon from '@material-ui/icons/Folder';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
import TrendingUpIcon from '@material-ui/icons/TrendingUp';


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
  const { labelText, labelIcon: LabelIcon, labelInfo, color, bgColor, ...other } = props;

  return (
    <TreeItem
      key={other.nodeId}
      label={
        <div className={classes.labelRoot}>
          <LabelIcon color="inherit" className={classes.labelIcon} />
          <Typography variant="body2" className={classes.labelText}>
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
  );
}

StyledTreeItem.propTypes = {
  bgColor: PropTypes.string,
  color: PropTypes.string,
  labelIcon: PropTypes.elementType.isRequired,
  labelInfo: PropTypes.string,
  labelText: PropTypes.string.isRequired,
};

const useStyles = makeStyles({
  root: {
    height: 264,
    flexGrow: 1,
    maxWidth: 400,
  },
});

export class VenResource extends React.Component {
  constructor( props ) {
    super( props );
    
  }

  render() {
    const {classes, resource} = this.props;
    var visitedNode = {};
    var venTreeIndex = [];
    var listExpended = [];
    for(var i in resource) {
      var res = resource[i];

      

      if(res.type === "VEN") {

        listExpended.push(res.id.toString())

      } else if(res.type === "REPORT") {

        listExpended.push(res.id.toString())

      } else if(res.type === "RESOURCE") {

        listExpended.push(res.id.toString())

      } else if(res.type === "ENDDEVICEASSET") {


      } else if(res.type === "REPORT_DESCRIPTION") {


      }

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
    for(var i in venTreeIndex) {

      var node= recursiveChildren(venTreeIndex[i]);
      venTree.push(node);
    }

    function recursiveView(node, index) {
      var ico;
      var indent = 16;
      if(node.resource.type === "VEN") {

        ico = SettingsInputComponentIcon;

      } else if(node.resource.type === "REPORT") {

        ico = AssessmentIcon;

      } else if(node.resource.type === "RESOURCE") {

        ico = FolderIcon;

      } else if(node.resource.type === "ENDDEVICEASSET") {

        ico = PowerSettingsNewIcon;

      } else if(node.resource.type === "REPORT_DESCRIPTION") {

        ico = TrendingUpIcon;

      }
      
      if(node.children && node.children.length > 0) {

          return (
            <StyledTreeItem  key={node.resource.id.toString()}  nodeId={node.resource.id.toString()} labelText={node.resource.venResourceLabel} labelIcon={ico} >
              {node.children && node.children.map(n => recursiveView(n, ++index))}
            </StyledTreeItem>
          );
      }
      else {
        return <StyledTreeItem key={node.resource.id.toString()}  nodeId={node.resource.id.toString()} labelText={node.resource.venResourceLabel} labelIcon={ico} />
      }
    }

    return (
    <div className={ classes.root }>
<TreeView
      className={classes.root}
      expanded={listExpended}
      defaultCollapseIcon={<ArrowDropDownIcon />}
      defaultExpandIcon={<ArrowRightIcon />}
>

      {venTree && venTree.map(n => recursiveView(n, 0))}

      </TreeView>

    </div>
    );
  }
}

export default VenResource;
