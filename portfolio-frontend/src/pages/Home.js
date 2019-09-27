import { Button } from '@material-ui/core';
import GridList from '@material-ui/core/GridList';
import { withStyles } from '@material-ui/styles';
import React from 'react';
import DialogFull from '../components/DialogFull';
import './Home.css';

const styles = (theme) => ({
    root: {
        marginTop: 30,
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
        width: 550,
        float: 'center',
        height: 550,
        display: 'flex',
    },
    gridText: {
        textAlign: 'center',
        float: 'center',
        verticalAlign: 'middle'
    }
});

class Home extends React.Component {
    render() {
        const { classes } = this.props;
        return (
            <div>
                <div className="App-Background"><DialogFull /></div>
                <div className="Title_Text">Hello</div>
                <div className="Text">
                    My Pages
                </div>
                <div className={classes.root}>
                    <GridList cellHeight={250} className={classes.gridList} cols={2}>
                        <Button className={classes.button}>
                            마이 페이지
                        </Button>
                        <Button className={classes.button} href="/introductions">
                            자기 소개서
                        </Button>
                        <Button className={classes.button} href="/projects">
                            프로젝트
                        </Button>
                        <Button className={classes.button}>
                            자격증
                        </Button>
                        )}
                    </GridList>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(Home);