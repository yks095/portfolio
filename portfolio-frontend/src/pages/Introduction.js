import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import { withStyles } from '@material-ui/styles';
import React from 'react';
import DialogFull from '../components/DialogFull';
import IntroductionDialog from '../components/introductions/IntroductionDialog';
import IntroductionAdd from '../components/introductions/IntroductionAdd';
import * as service from '../service/introduction';
import './Introduction.css';

const styles = (theme) => ({
    root: {
        marginTop: 100,
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'center',
        overflow: 'hidden',
    },
    button: {
        fontSize: '200%',
        fontWeight: 'bold',
    },
    gridList: {
        width: 1000,
        float: 'center',
        height: 550,
        display: 'flex',
    },
    gridText: {
        textAlign: 'center',
        float: 'center',
        verticalAlign: 'middle'
    },
});

class Introduction extends React.Component {
    constructor(props) {
        super();
        this.state = {
            introduction: "",
        }
    }

    componentDidMount() {
        this.fetchIntroductionInfo();
    }

    fetchIntroductionInfo = async () => {
        const introduction = await service.getIntroduction();
        this.setState({
            'introductions': introduction.data._embedded.introductionList
        });
    }
    render() {
        const { classes } = this.props;
        return (
            <div >
                <div className="Introduction-Background">
                    <DialogFull />
                    <div className="Title_Text_I">Introduction</div>
                    <IntroductionAdd />
                    <div className={classes.root}>
                        <GridList cellHeight={250} className={classes.gridList} cols={3}>
                            {
                                this.state.introductions ?
                                    this.state.introductions.map(c => {
                                        return (
                                            <GridListTile key={c.idx} cols={c.cols || 1}>
                                                <IntroductionDialog
                                                    key={c.idx}
                                                    onClick={this.handleClickOpen}
                                                    idx={c.idx}
                                                    introductionTitle={c.introductionTitle}
                                                    title1={c.title1}
                                                    content1={c.content1}
                                                    title2={c.title2}
                                                    content2={c.content2}
                                                    title3={c.title3}
                                                    content3={c.content3}
                                                    title4={c.title4}
                                                    content4={c.content4}
                                                    title5={c.title5}
                                                    content5={c.content5}
                                                >
                                                </IntroductionDialog>
                                            </GridListTile>

                                        )
                                    }) : ""
                            }
                        </GridList>
                    </div>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(Introduction);