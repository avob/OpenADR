import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';
import VenList from '../Ven/VenList'
import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 500,
  },
  card: {
    maxWidth: 350,
    minWidth: 350,
  },
  media: {
    height: 40,
    paddingTop: 10,
    paddingRight: 10
  },
  button: {
    margin: theme.spacing.unit,
  },
  iconButton: {
    marginTop: 10
  },
});

export class VenPage extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
    this.state.filters = [];
    this.state.pagination = {
      page: 0
      , size: 20
    }
  }

  componentDidMount() {
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.venActions.searchVen(this.state.filters, this.state.page, this.state.size);

  }

  onFilterChange = (filters) => {
    this.setState({filters});
    this.props.venActions.searchVen(filters, this.state.page, this.state.size);
  }

  onPaginationChange = (pagination) => {
    this.setState({pagination});
  }

  render() {
    const {classes, ven} = this.props;
    return (
    <div className={ classes.root }>
      <Tabs value={ 0 }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="VENs" />
      </Tabs>
      <Divider variant="middle" />
      <Typography component="div" style={ { padding: 8 * 3 } }>
        <VenList classes={ classes }
                 ven={ ven.ven }
                 marketContext={ ven.marketContext }
                 group={ ven.group }
                 deleteVen={ this.props.venActions.deleteVen } 
                 filters={this.state.filters}
                 pagination={this.state.pagination}
                 onFilterChange={this.onFilterChange}
                 onPaginationChange={this.onPaginationChange}/>
      </Typography>
    </div>

    );
  }
}

VenPage.propTypes = {
  venActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    ven: state.ven
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    venActions: bindActionCreators( venActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenPage ) );
