import React from 'react';
import IntroductionPanel from '../components/introductions/IntroductionPanel';
import IntroductionAdd from '../components/introductions/IntroductionAdd';
import DialogFull from '../components/DialogFull';
import * as service from '../service/introduction';


class Introduction extends React.Component {

    constructor(props) {
        super();
        this.state = {
            introduction: ""
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
        return (
            <div>
                <DialogFull />
                <div>
                    {
                        this.state.introductions ?
                            this.state.introductions.map(c => {
                                return (<IntroductionPanel
                                    key={c.idx}
                                    idx={c.idx}
                                    title={c.title}
                                    growth={c.growth}
                                    reason={c.reason}
                                    strength={c.strength}
                                    weakness={c.weakness}
                                    aspiration={c.aspiration}
                                />
                                )
                            }) : ""
                    }
                    <IntroductionAdd />
                </div>
            </div>
        );
    }
}

export default Introduction;