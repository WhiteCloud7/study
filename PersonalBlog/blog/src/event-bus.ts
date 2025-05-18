type EventCallback<T = any> = (data: T) => void;

class EventBus {
    private events: Record<string, EventCallback[]> = {};

    // 注册事件
    on<T = any>(event: string, callback: EventCallback<T>): void {
        if (!this.events[event]) {
            this.events[event] = [];
        }
        this.events[event].push(callback);
    }

    // 触发事件
    emit<T = any>(event: string, data: T): void {
        if (this.events[event]) {
            this.events[event].forEach(callback => callback(data));
        }
    }

    // 移除事件
    off<T = any>(event: string, callback: EventCallback<T>): void {
        if (this.events[event]) {
            this.events[event] = this.events[event].filter(cb => cb !== callback);
        }
    }
}

export const eventBus = new EventBus();
